# Boxwise roadmap (Android)

## Phase 0 — Scaffold (done Sep 28, 2026)
- [x] Gradle project (Kotlin, Compose Material 3, Room, Navigation, ZXing)
- [x] Room schema: boxes, items, global search query
- [x] Four screens: boxes list, box detail, search, QR label (+ share-as-PNG)

## Phase 1 — MVP v0.1 (~4 weekends)
- [ ] Photo capture per box/item (CameraX + FileProvider — deps already added)
- [ ] Edit/delete boxes and items; move items between boxes
- [ ] `boxwise://box/{id}` deep link so scanning a label opens the box
- [ ] Google Sheet importer for the existing garage inventory

## Phase 2 — Polish v0.2
- [ ] Home-screen widget: "where is X?" search shortcut
- [ ] CSV/PDF export of box contents (insurance)
- [ ] Proper print layout for QR labels (instead of share-as-PNG)

## Phase 3 — Launch
- [ ] Signed APK on GitHub Releases (or Play internal testing track)
- [ ] Launch post
- [ ] Dogfood: migrate the real garage inventory off the Sheet
