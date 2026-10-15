# Inventory Coverage

## Baseline (existing tests, before additions)

| Metric | Covered | Total | % |
|---|---|---|---|
| Instructions | 33/44 | 44 | 75% |
| Branches | 8/16 | 16 | 50% |
| Lines | 15/20 | 20 | 75% |
| Methods | 6/6 | 6 | 100% |

Baseline gaps: the `add`-when-full path, `remove` not-found path,
`isFull` false branch, `contains` not-found path, and all
empty/zero-capacity loop exits were never executed.

## Final (after added tests)

| Metric | Covered | Total | % |
|---|---|---|---|
| Instructions | 44/44 | 44 | 100% |
| Branches | 16/16 | 16 | 100% |
| Lines | 20/20 | 20 | 100% |
| Methods | 6/6 | 6 | 100% |

## Tests added

Tests cover both outcomes of every branch, plus empty and full inventories:

- ConstructorTests: slot allocation, zero capacity.
- GetTests: valid index, out-of-bounds `ArrayIndexOutOfBoundsException`.
- AddTests: first-fit placement, skip-occupied, drop-when-full, no-op on zero capacity.
- RemoveTests: first match cleared, duplicate handled, absent item no-op,
  identity (not equals) matching, empty inventory no-op.
- IsFullTests: true when full, false when partial, true when vacuous (size 0).
- ContainsTests: present, absent, empty inventory, identity matching.

## Remaining uncovered behavior

None for statement/line or branch coverage.

