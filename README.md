# GameModel - V2
## Duas entidades independentes:

### Class: GameModel (id, name, console, owner)
```Java 
@Id
@GeneratedValue(strategy = GenerationType.AUTO)
private Long id;

@Column(nullable = false, length = 100)
private String name;

@Enumerated(EnumType.STRING)
@Column(nullable = false, length = 50)
private ConsoleEnum console;

@ManyToOne
@JoinColumn(name = "owner_id")
@NotNull
private PartnerModel owner;
 ```
### Class: Partner (id, name, phoneNumber)
```Java 
@Id
@GeneratedValue(strategy = GenerationType.AUTO)
private Long id;

@Column(nullable = false, length = 100)
private String name;

@Column(nullable = false, length = 15)
private String phoneNumber;
 ```

## Restrição:
  Para se associar e manter-se associado, cada membro deve ter pelo menos um jogo no catálogo.


## DTO's
### Class: GameDto
```Java 
@NotBlank
@Size(max = 100)
private String name;

private ConsoleEnum console;
 ```

### Class: GameDtoOutput
```Java 
private Long id;
private String name;
private ConsoleEnum console;
 ```

### Class: GamePartnerDto
```Java 
@NotBlank
@Size(max = 100)
private String name;

private ConsoleEnum console;

@NotBlank
@Size(max = 100)
private String owner;

@NotBlank
@Size(max = 15)
private String ownerPhoneNumber ;
 ```
 
### Class: PartnerDto
```Java 
@NotBlank
@Size(max = 100)
private String name;

@NotBlank
@Size(max = 15)
private String phoneNumber ;
 ```
## Database Postgres
    database name: gamers-club-db-v2

## Routes
### Game
```Java 
- GET /v1/game
    return Page<GameModel>()

- GET /v1/game/{game_id}
    game_id: Long
    return GameModel()

- GET /v1/game/search/{console_name}?name={game_name}
    console_name: PS3, PS4, PS5, XBOX, XBOXONE, PC
    game_name: type String
    return Page<GameModel>()

- GET /v1/game/search?name={game_name}&console={console_name}
    console_name: PS3, PS4, PS5, XBOX, XBOXONE, PC
    game_name: type String
    return Page<GameModel>()

- POST /v1/game
    Example: GameDto() { "name": "Need For Speed 2",
      "console": "XBOX", "owner": "Lucas Carvalho",
      "ownerPhoneNumber": "67 9999-9999" }
    return GameModel()

- PUT /v1/game/{id}
  Example JSON: GameDto() { "name": "Need For Speed 2",
    "console": "XBOX", "owner": "Lucas Candido", 
    "ownerPhoneNumber": "67 99699-9999" }
  return Page<GameModel>()


- DELETE /v1/game/{id}
    game_id: Long
    return void
 ```

### Partner
```Java 
- GET /v1/partner
    return Page<PartnerModel>()

- GET /v1/partner/{partner_id}
    partner_id: Long
    return PartnerModel()


- GET /v1/partner/search/?name={partner_name}&phoneNumber={partner_phoneNumber}
    partner_name: String (Optional)
    partner_phoneNumber: String (Optional)
    return Page<PartnerModel>()


- POST *** no route ***
    *** Partner need game *** 


- PUT /v1/partner/{partner_id}
    partner_id: Long
    Example JSON: PartnerDto() { "name": "Alexandro Carvalho",
  "phoneNumber": "67 9999-9999" }
    return Page\<PartnerModel\>()


- DELETE /v1/partner/{partner_id}
    partner_id: Long
    return void


- GET /v1/partner/{partner_id}/game
    partner_id: Long
    return Page<GameModelDtoOutput>()


- GET /v1/partner/{partner_id}/game/search?name={game_name}&console={console_name}
    partner_id: Long
    game_name: String (Optional)
    console_name: String (Optional)
    return Page<GameModelDtoOutput>()


- POST /v1/partner/{partner_id}/game
    Example: GameDto() { "name": "Need For Speed 2",
    "console": "XBOX" }
  
    return GameModel()
 ```

### Route Get with return Page\<?\> (Optional)
    @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable
    Example
      GET /v1/partner?page=0,size=10,sort="id",direction=ASC

## Import with Postman
- [Link Postman Share](https://www.getpostman.com/collections/cd3427d28ded214ea868)

## Authors
- [walmirlsj1](https://github.com/walmirlsj1)
 