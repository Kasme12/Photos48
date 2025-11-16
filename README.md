# Photos48

Photos48 is a simple JavaFX photo manager. It lets you create users, manage albums, import photos (with background copy + thumbnails), view/zoom photos, and search by date or tags. Built with Maven on JDK 21 + JavaFX 21.

## Features

- Albums and photos with captions and tags
- Background import with progress; optional copy-on-import to user workspace
- Disk thumbnail cache with automatic regeneration on source updates
- Photo viewer with zoom controls (buttons + slider)
- Search by date range and by tags (AND/OR, case-insensitive)
- Admin screen (login as `admin`) to create/delete users
- Stock user `stock` with sample images from `./data`

## Requirements

- JDK 21
- Maven 3.9+
- Java FX SDK 21
## Run

Run the app from the project root:

```powershell
mvn -DskipTests javafx:run
```

Build the jar (outputs to `target/`):

```powershell
mvn -DskipTests package
```

Generate Javadoc into `./docs`:

```powershell
mvn -DskipTests verify
```

## Quick Test

1. Launch the app: `mvn -DskipTests javafx:run`.
2. At login:
	- Enter `stock` to explore a sample album (auto-created from `./data`, placeholders generated if `./data` is empty), or
	- Enter any new username to create a fresh account, or
	- Enter `admin` to manage users (create/delete; cannot delete `admin`).
3. From User Home, open or create an album.
4. In an album:
	- Click Add Photo to import files. A progress bar shows background copy.
	- Open a photo to view; use zoom in/out/reset and the slider.
	- Edit caption/tags; search tags or date from the Search screen. Double-click a result to open the photo.
	- Remove Photo moves the file safely to a per-user `.trash` folder (if it was copied in).

## Storage & Folders

- App data (users, albums, photos): `~/.photos48` (on Windows: `C:\Users\<you>\.photos48`)
- Thumbnails cache: `~/.photos48/.thumbnails`
- Orphaned/removed imported files: `~/.photos48/.trash`
- Stock images input: `./data` (drop `jpg/jpeg/png/bmp/gif` here; placeholders are auto-generated if empty)
- Project Javadoc output: `./docs` (open `./docs/index.html`)

## Entry Point

- Main class: `photos48.ui.Photos`

## Notes

- This project uses Maven only. Any Gradle files or the `build/` directory are not needed and have been removed. Use `target/` artifacts for builds.

