# CSV parser demo app

## Parser

Parser module consists of two parts:

* API module
* CSV implementation module

### Parser API module

Provides parser interface definition. Parser expecting to receive `Reader` in order to provided the flexibility of input source and avoid large memory consumption, while it depends on the implementation. It can be a file, an in-memory string, a network stream, etc.

### Parser implementation module

Provides CSV parser implementation and utilizes `API` module as api dependency.
Current implementation ignores any mismatches in row lengths and returns list instead of array

## Application

Given demo application parses the CSV file provided from `raw` resources, stores them in in-memory storage and displays output as a table. First row of CSV is expected to be the headers row.

### Structure

Application consists of following components:

* Data classes
* Repository
* Use cases
* View model
* Dagger DI

#### Data classes

* `Row` represents a single row of `Cell`s. It is converted from `List<String>`.
* `Cell` represents a single item in the `Row`. It is converted from single `String`.

#### Repository

* `IRepository` - defines interface for application repository. This will be used in testing and allows to provide additional implementations like `SqLiteRepositor`, `JsonFileRepository`, `RoomRepository`, etc.
* `InMemoryRepository` - simple implementation of repository for demo purposes.

#### Use cases

Pieces of business logic that incapsulate in application logic. This allows business logic testing without dependency on the external and application-specific implementation.

* `LoadAndSaveRecordsUseCase` - loads data from `csv` and stores the into repository. If data already present in repository, then this will turn into `no-op`. It is possible to force data retrieval via `force` parameter.
* `ReadHeadersUseCase` - retrieves headers `Row` from repository
* `ReadRowsUseCase` - retrieves data `Row`s from repository

#### View model

* MainViewModel - contains and handles use-cases, `LiveData`, view states, and UI logic related to the `MainFragment` (which is the only screen in the app).

Because we've separated business logic into stand alone units, it is possible to test view model separately from `*UseCase` implementation.
Also, `*ViewModel` allows us to test UI without dependency on the `*ViewModel` implementation

#### Dagger DI

All the dependencies are provided and injected via `Dagger` DI. This allows us to swap real implementation for mocked implementation for testing.

### Notes

Most of the methods in classes are declared `open` in order to provided mocking possiblity. It can be done via interfaces, while I think it is a bit an overkill for this simple demo app.