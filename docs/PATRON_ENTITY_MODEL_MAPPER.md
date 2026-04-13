# Patrón Entity/Model/Mapper en Tech-Cup Backend

## 1. Ubicación de Mappers

### Mappers de Controller (DTO ↔ Model)
**Ruta**: `src/main/java/.../controller/mapper/`

Mappers que convierten entre DTOs (para HTTP) y Models (objeto de dominio):
- `UserMapper.java` - DTO ↔ Model de Usuario
- `TournamentMapper.java` - DTO ↔ Model de Torneo
- `TeamMapper.java` - DTO ↔ Model de Equipo
- `SportProfileMapper.java` - DTO ↔ Model de Perfil Deportivo
- `MatchPersistenceMapper.java` - DTOs relacionados a Partidos

**Ejemplo**: [TournamentMapper.java](../src/main/java/eci/edu/co/Tech_Cup_DOSW_BackEnd_2026_1/controller/mapper/TournamentMapper.java)
```java
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TournamentMapper {
    Tournament toEntity(TournamentRequest request);
    TournamentResponse toResponse(Tournament entity);
}
```

### Mappers de Persistence (Entity ↔ Model)
**Ruta**: `src/main/java/.../persistence/mapper/`

Mappers que convierten entre Entities (JPA/BD) y Models (dominio):
- `TournamentPersistenceMapper.java` - Entity ↔ Model
- `TeamPersistenceMapper.java` - Entity ↔ Model
- `UserPersistenceMapper.java` - Entity ↔ Model
- `MatchPersistenceMapper.java` - Entity ↔ Model

**Ejemplo**: [TournamentPersistenceMapper.java](../src/main/java/eci/edu/co/Tech_Cup_DOSW_BackEnd_2026_1/persistence/mapper/TournamentPersistenceMapper.java)
```java
@Mapper(componentModel = "spring", uses = {TeamPersistenceMapper.class}, 
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TournamentPersistenceMapper {
    TournamentEntity toEntity(Tournament model);
    Tournament toModel(TournamentEntity entity);
}
```

---

## 2. Tipado de Repositorios

### Los repositorios SOLO aceptan Entities (NO Models)

**Ruta**: `src/main/java/.../persistence/repository/`

Los repositorios están parametrizados con **Entity**, no con Model:

```java
// ✅ CORRECTO - acepta TournamentEntity
@Repository
public interface TournamentRepository extends JpaRepository<TournamentEntity, Long> {
}

// ✅ CORRECTO - acepta UserEntity
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
}

// ❌ INCORRECTO - NO usar Model aquí
// public interface TournamentRepository extends JpaRepository<Tournament, Long> {}
```

### Flujo de tipado en Service

```java
@Service
@RequiredArgsConstructor
public class TournamentServiceImpl {
    
    // Los repositorios reciben Entities
    private final TournamentRepository tournamentRepository;      // JpaRepository<TournamentEntity, Long>
    
    // Los mappers convierten entre tipos
    private final TournamentPersistenceMapper persistenceMapper;  // Entity ↔ Model
    private final TournamentMapper controllerMapper;              // DTO ↔ Model
    
    public TournamentResponse create(TournamentRequest request) {
        // 1. DTO → Model (controllerMapper)
        Tournament model = controllerMapper.toEntity(request);
        
        // 2. Model → Entity (persistenceMapper)
        // 3. Entity → BD (repository)
        TournamentEntity saved = tournamentRepository.save(
            persistenceMapper.toEntity(model)
        );
        
        // 4. Entity → Model (persistenceMapper)
        Tournament savedModel = persistenceMapper.toModel(saved);
        
        // 5. Model → Response DTO (controllerMapper)
        return controllerMapper.toResponse(savedModel);
    }
}
```

---

## 3. Estructura de Clases (Comparativa)

### Entity (Persistencia)
**Ubicación**: `persistence/entity/`
- Anotación `@Entity` y `@Table`
- Anotaciones `@Column`, `@Id`, `@GeneratedValue`
- Annotations JPA para relaciones

```java
@Entity
@Table(name = "tournaments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TournamentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(columnDefinition = "TEXT")
    private String rules;
    
    @Enumerated(EnumType.STRING)
    private TournamentStatus status;
}
```

### Model (Dominio)
**Ubicación**: `core/model/`
- POJO simple sin anotaciones JPA
- Representación en memoria del dominio
- Usado por Services y Controllers

```java
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tournament {
    private Long id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private int teamCount;
    private double costPerTeam;
    private String rules;
    private TournamentStatus status;
}
```

### DTO Request
**Ubicación**: `controller/dto/request/`
- Validación con `@NotNull`, `@Size`, etc.
- Solo campos que acepta el cliente

```java
@Data
@Builder
public class TournamentRequest {
    @NotBlank(message = "Tournament name is required")
    private String name;
    
    @NotNull(message = "Start date is required")
    private LocalDate startDate;
    
    private double costPerTeam;
}
```

### DTO Response
**Ubicación**: `controller/dto/response/`
- Solo campos que retorna el servidor
- Puede incluir datos derivados

```java
@Data
@Builder
public class TournamentResponse {
    private Long id;
    private String name;
    private LocalDate startDate;
    private TournamentStatus status;
}
```

---

## 4. Patrón Correcto en Tests

### Test de Mapper (Unitario sin Spring)

```java
@DisplayName("TeamMapper Tests")
class TeamMapperTest {
    
    private TeamMapper teamMapper;
    
    @BeforeEach
    void setUp() {
        // Usar Mappers.getMapper() para instancias de prueba
        teamMapper = Mappers.getMapper(TeamMapper.class);
    }
    
    @Test
    @DisplayName("Should convert TeamRequest to Team entity successfully")
    void testToEntity() {
        // Arrange
        TeamRequest request = TeamRequest.builder()
            .name("Los Tigres FC")
            .shieldUrl("https://example.com/shield.png")
            .tournamentId(1L)
            .build();
        
        // Act
        Team mappedTeam = teamMapper.toEntity(request);
        
        // Assert
        assertThat(mappedTeam)
            .isNotNull()
            .extracting("name", "shieldUrl", "tournamentId")
            .containsExactly("Los Tigres FC", "https://example.com/shield.png", 1L);
    }
    
    @Test
    @DisplayName("Should convert Team entity to TeamResponse successfully")
    void testToResponse() {
        // Arrange
        Team teamEntity = Team.builder()
            .id(10L)
            .name("Los Tigres FC")
            .shieldUrl("https://example.com/shield.png")
            .tournamentId(1L)
            .build();
        
        // Act
        TeamResponse response = teamMapper.toResponse(teamEntity);
        
        // Assert
        assertThat(response)
            .isNotNull()
            .extracting("id", "name", "shieldUrl")
            .containsExactly(10L, "Los Tigres FC", "https://example.com/shield.png");
    }
    
    @Test
    @DisplayName("Should handle null TeamRequest in toEntity")
    void testToEntityWithNull() {
        // Act
        Team mappedTeam = teamMapper.toEntity(null);
        
        // Assert
        assertNull(mappedTeam);
    }
}
```

### Test de Mapper (Integración con Spring)

```java
@SpringBootTest
@DisplayName("TournamentMapper Tests")
class TournamentMapperTest {
    
    @Autowired
    private TournamentMapper tournamentMapper;
    
    @Test
    @DisplayName("Debe mapear TournamentRequest a entidad Tournament")
    void testToEntity() {
        TournamentRequest request = TournamentRequest.builder()
            .name("Copa DOSW 2026")
            .startDate(LocalDate.of(2026, 4, 1))
            .endDate(LocalDate.of(2026, 6, 30))
            .teamCount(8)
            .costPerTeam(300000.0)
            .build();
        
        Tournament entity = tournamentMapper.toEntity(request);
        
        assertNotNull(entity);
        assertEquals("Copa DOSW 2026", entity.getName());
        assertEquals(LocalDate.of(2026, 4, 1), entity.getStartDate());
        assertEquals(8, entity.getTeamCount());
        assertNull(entity.getId());  // Sin guardar en BD
    }
    
    @Test
    @DisplayName("Debe mapear entidad Tournament a TournamentResponse")
    void testToResponse() {
        Tournament entity = Tournament.builder()
            .id(1L)
            .name("Copa DOSW 2026")
            .startDate(LocalDate.of(2026, 4, 1))
            .status(TournamentStatus.DRAFT)
            .build();
        
        TournamentResponse response = tournamentMapper.toResponse(entity);
        
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Copa DOSW 2026", response.getName());
        assertEquals(TournamentStatus.DRAFT, response.getStatus());
    }
}
```

### Test de Service (Con Mocks)

```java
@ExtendWith(MockitoExtension.class)
@DisplayName("TournamentServiceImpl Tests")
class TournamentServiceImplTest {
    
    @Mock
    private TournamentRepository tournamentRepository;
    
    @Mock
    private TournamentPersistenceMapper persistenceMapper;
    
    @Mock
    private TournamentMapper controllerMapper;
    
    @InjectMocks
    private TournamentServiceImpl tournamentService;
    
    @Test
    @DisplayName("Debe crear torneo exitosamente")
    void testCreate() {
        // Arrange
        TournamentRequest request = TournamentRequest.builder()
            .name("Copa 2026")
            .startDate(LocalDate.of(2026, 4, 1))
            .build();
        
        Tournament model = Tournament.builder()
            .name("Copa 2026")
            .startDate(LocalDate.of(2026, 4, 1))
            .status(TournamentStatus.DRAFT)
            .build();
        
        TournamentEntity entity = new TournamentEntity();
        entity.setId(1L);
        
        TournamentResponse response = TournamentResponse.builder()
            .id(1L)
            .name("Copa 2026")
            .build();
        
        // Mock del flujo
        when(controllerMapper.toEntity(request)).thenReturn(model);
        when(persistenceMapper.toEntity(model)).thenReturn(entity);
        when(tournamentRepository.save(entity)).thenReturn(entity);
        when(persistenceMapper.toModel(entity)).thenReturn(model);
        when(controllerMapper.toResponse(model)).thenReturn(response);
        
        // Act
        TournamentResponse result = tournamentService.create(request);
        
        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Copa 2026", result.getName());
        
        verify(tournamentRepository, times(1)).save(entity);
    }
}
```

---

## 5. Resumen del Flujo Completo

```
┌──────────────────┐
│  HTTP Request    │
│  (TournamentDTO) │
└────────┬─────────┘
         │ TournamentMapper.toEntity()
         ▼
┌──────────────────┐
│  Model           │
│  (Tournament)    │  ◄─── Objeto de dominio
└────────┬─────────┘
         │ TournamentPersistenceMapper.toEntity()
         ▼
┌──────────────────┐
│  JPA Entity      │
│  (TournamentEnt.)│
└────────┬─────────┘
         │ TournamentRepository.save()
         ▼
┌──────────────────┐
│  Database        │
│  (tournaments)   │
└────────┬─────────┘
         │ tournamentRepository.findById()
         ▼
┌──────────────────┐
│  JPA Entity      │
│  (TournamentEnt.)│
└────────┬─────────┘
         │ TournamentPersistenceMapper.toModel()
         ▼
┌──────────────────┐
│  Model           │
│  (Tournament)    │
└────────┬─────────┘
         │ TournamentMapper.toResponse()
         ▼
┌──────────────────┐
│  HTTP Response   │
│  (TournamentDTO) │
└──────────────────┘
```

---

## 6. Archivos de Referencia

### Mappers principales
- [TournamentMapper.java](../src/main/java/eci/edu/co/Tech_Cup_DOSW_BackEnd_2026_1/controller/mapper/TournamentMapper.java)
- [TournamentPersistenceMapper.java](../src/main/java/eci/edu/co/Tech_Cup_DOSW_BackEnd_2026_1/persistence/mapper/TournamentPersistenceMapper.java)
- [TeamMapper.java](../src/main/java/eci/edu/co/Tech_Cup_DOSW_BackEnd_2026_1/controller/mapper/TeamMapper.java)

### Repositorios principales
- [TournamentRepository.java](../src/main/java/eci/edu/co/Tech_Cup_DOSW_BackEnd_2026_1/persistence/repository/TournamentRepository.java)
- [UserRepository.java](../src/main/java/eci/edu/co/Tech_Cup_DOSW_BackEnd_2026_1/persistence/repository/UserRepository.java)
- [TeamRepository.java](../src/main/java/eci/edu/co/Tech_Cup_DOSW_BackEnd_2026_1/persistence/repository/TeamRepository.java)

### Tests de ejemplo
- [TournamentMapperTest.java](../src/test/java/eci/edu/co/Tech_Cup_DOSW_BackEnd_2026_1/controller/mapper/TournamentMapperTest.java)
- [TeamMapperTest.java](../src/test/java/eci/edu/co/Tech_Cup_DOSW_BackEnd_2026_1/controller/mapper/TeamMapperTest.java)
- [TournamentServiceImplTest.java](../src/test/java/eci/edu/co/Tech_Cup_DOSW_BackEnd_2026_1/service/impl/TournamentServiceImplTest.java)

### Servicios principales
- [TournamentServiceImpl.java](../src/main/java/eci/edu/co/Tech_Cup_DOSW_BackEnd_2026_1/core/service/impl/TournamentServiceImpl.java)

---

## 7. Reglas Clave

| Regla | Descripción |
|-------|-------------|
| **Repositorios** | SIEMPRE usan `Entity` como tipo genérico, NUNCA `Model` |
| **Mappers Unitarios** | Usar `Mappers.getMapper()` sin Spring para tests simples |
| **Mappers con Spring** | Usar `@Autowired` e inyectar en tests con `@SpringBootTest` |
| **Service → BD** | Flujo: `DTO` → Mapper Controller → `Model` → Mapper Persistence → `Entity` → Repository |
| **BD → Service** | Flujo: Repository → `Entity` → Mapper Persistence → `Model` → Mapper Controller → `DTO` |
| **Sin Business Logic en Mappers** | Los mappers solo convierten campos, SIN lógica de negocio |
| **Validación antes de Mapper** | Validar DTOs ANTES de mapear a Model/Entity |

