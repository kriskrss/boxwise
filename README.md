# Boxwise (Android)

Home storage, organized — as a native Android app. Every box numbered,
photographed, and searchable. Born from a real Google Sheet ('Garage storage');
this turns that sheet into an app that lives in your pocket in the garage:
boxes, items, full-text search ("where is the Diwali stuff?"), and printable
QR labels you stick on each box. Scan a label, see what's inside.

## Stack

- **Kotlin** + **Jetpack Compose** (Material 3) — Google's modern Android path
- **Room** (SQLite) — boxes and items, observed as Flows
- **Navigation Compose** — boxes → box detail → search → QR label
- **ZXing** — QR code generation; labels export as PNG via the share sheet
  (which includes printers, so share = print for v0.1)
- **CameraX** — on the roadmap for photo capture (Phase 1)

## Run it

1. Install [Android Studio](https://developer.android.com/studio) (JDK 17 bundled).
2. **File → Open** this `boxwise/` directory. Let Gradle sync finish.
3. **Run → Run 'app'** on an emulator (Pixel, API 34+) or your own device.

No API keys, no backend, no accounts. The database is a local Room file.

## New to Android? Start here

You're a Staff SWE, so Kotlin will click fast (null-safety, data classes,
coroutines ≈ async/await with structure). The genuinely new ideas are four:

1. **Composable functions** — UI is functions, not XML. `BoxesScreen.kt` is
   the whole first screen; read it top to bottom.
2. **State** — `remember { mutableStateOf(...) }` + `collectAsState()` on Room
   Flows. When the DB changes, the list re-renders. That's the whole pattern.
3. **Room** — `data/` has the schema. `@Dao` interfaces are the API;
   `BoxwiseDatabase` is the singleton.
4. **Navigation** — routes are strings in `MainActivity.kt` (`"box/{id}"`).

Then do the official [Jetpack Compose basics codelab](https://developer.android.com/codelabs/jetpack-compose-basics?authuser=2&%3Bhl=it) —
about two hours, and you'll read this codebase fluently.

## What's wired (v0.1 scaffold)

- [x] Boxes list + add box (Room + Compose + dialog)
- [x] Box detail: items list + add item
- [x] Global search across all boxes
- [x] QR label screen per box, share-as-PNG (print via share sheet)
- [ ] Photo capture per box/item (CameraX — next)
- [ ] Edit/delete, move items between boxes
- [ ] `boxwise://box/{id}` deep link (scan a label → opens the box)
- [ ] Google Sheet importer for the existing inventory

## Layout

```
app/src/main/java/com/boxwise/app/
  MainActivity.kt        navigation graph
  BoxwiseApp.kt          Application: Room singleton
  data/                  Room entities, DAOs, database
  ui/                    Compose screens (Boxes, Detail, Search, QR label)
  ui/theme/              Material 3 theme
  util/QrCode.kt         ZXing → Compose ImageBitmap
```

## Shipping bar

- [ ] Public repo with this README
- [ ] APK on the Releases page (or Play internal track)
- [ ] One launch post
- [ ] Dogfood milestone: the real garage inventory migrated off the Sheet
