# Rebel Focus — Migration Strategy

## Current version

**Database version: 1** — Initial schema.

## v1 approach

During active development, `fallbackToDestructiveMigration()` is used in the Room builder.
This means any schema change wipes the database, which is acceptable before real users exist.

## Pre-release migration rules

Before the first sideloaded APK reaches real users:

1. **Remove `fallbackToDestructiveMigration()`** from `DatabaseModule.kt`.
2. **Add explicit `Migration(1, 2)`** objects for every schema change.
3. **Enable `exportSchema = true`** (already set) so Room generates JSON schema files in `app/schemas/`.
4. **Write instrumentation tests** using `MigrationTestHelper` to validate each migration.
5. **Never drop user data** — every migration must preserve existing rows.

## Schema export

Room's `exportSchema = true` is configured in `RebelFocusDatabase.kt`.
The Gradle `ksp` block should include:

```kotlin
ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}
```

This generates versioned JSON schema files that:
- serve as the migration baseline,
- enable `MigrationTestHelper` in instrumentation tests,
- provide a diff-friendly audit trail of schema evolution.

## Future migration patterns

| Change type | Migration approach |
|---|---|
| Add a new column with a default | `ALTER TABLE ... ADD COLUMN ... DEFAULT ...` |
| Add a new table | `CREATE TABLE IF NOT EXISTS ...` |
| Rename a column | `ALTER TABLE ... RENAME COLUMN ...` (API 30+ SQLite) or recreate table |
| Remove a column | Recreate table with new schema, copy data, drop old, rename |
| Change column type | Recreate table |

## Conflict with sync

When Drive sync is implemented (Phase 8), migrations must also consider:
- imported data from older app versions,
- schema version field in backup payloads,
- validation before restoring data into a newer schema.

## Testing

Migration tests should use:
- `MigrationTestHelper` from `androidx.room:room-testing`
- A test database seeded with known data at version N
- Assertion that data survives migration to version N+1
