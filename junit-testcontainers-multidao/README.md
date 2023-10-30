# Ease of mind in large scale database migration through JUnit5 parameterized test

In this repository you will find a simple example of how to use JUnit5s parameterized tests to ensure that a new version
of a certain DAO behaves the same as an older one after migrating the SQL clauses in said DAO to a syntax supported by
the new RDBMS. This was found to be useful in a migration project where no abstraction layer was used, rather raw SQL
was hardcoded to different parts of the system.

### Pros
- Easy to build against existing integration tests
- Other version of DAO can safely live in src/test

### Cons
- Need to track changes to the production version of the DAO and reflect them to the new one
- Scaffolding built has to be removed when migration is done