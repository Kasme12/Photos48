# PhotosXX

A JavaFX desktop application for managing photo albums, built for CS213. Users can log in, create albums, add photos, tag and caption them, and search across albums by date or tags. Admins can manage users.

## 👩‍💻 Authors
- Esmeralda Bencosme
- [Partner Name Here]

## 🚀 How to Run
1. Open the project in IntelliJ
2. Run `Photos.java` from `src/model`
3. Login as:
   - `admin` → access admin dashboard
   - any created username → access user dashboard

## 🗂 Project Structure
```bash
PhotosXX/
├── data/                          ← Stores serialized user data
│   └── users.dat                  ← Automatically created file storing all users, albums, and photos
│
├── docs/                          ← Javadoc HTML output
│   └── index.html                 ← Entry point for generated documentation
│   └── model/*.html               ← One page per model class (User.html, Album.html, etc.)
│
├── src/
│   ├── model/                     ← Core data classes and app launcher
│   │   ├── Admin.java             ← Represents the admin user (optional, may be merged with User)
│   │   ├── Album.java             ← Represents a photo album with a list of Photo objects
│   │   ├── DataStore.java         ← Handles saving/loading users.dat using serialization
│   │   ├── Photo.java             ← Represents a photo with caption, tags, and metadata
│   │   ├── PhotoManager.java      ← Central manager for all users; used by controllers
│   │   ├── Tag.java               ← Represents a tag (type=value) attached to a photo
│   │   ├── User.java              ← Represents a regular user with a list of albums
│   │   └── Photos.java            ← Main launcher class with `main()` method
│
│   ├── controller/                ← JavaFX controllers for each screen
│   │   ├── AdminController.java   ← Handles admin actions: create/delete users
│   │   ├── AlbumController.java   ← Handles album actions: add/remove/open photos
│   │   ├── LoginController.java   ← Handles login logic for admin and users
│   │   ├── PhotoController.java   ← Handles photo view: caption, tags, date
│   │   ├── SearchController.java  ← Handles search by date/tags and album creation
│   │   └── UserController.java    ← Handles user dashboard: album management
│
│   ├── view/                      ← FXML layout files for each screen
│   │   ├── admin.fxml             ← Admin dashboard UI
│   │   ├── album.fxml             ← Album view UI
│   │   ├── login.fxml             ← Login screen UI
│   │   ├── photo.fxml             ← Photo viewer UI
│   │   ├── search.fxml            ← Search screen UI
│   │   └── user.fxml              ← User dashboard UI
│
│   └── resources/                 ← Optional: place for images, icons, or CSS
│       └── (optional files)       ← e.g. logo.png, styles.css
│
├── README.md                      ← Project overview, instructions, and test checklist
├── .gitignore                     ← Optional: ignore data/users.dat and compiled files
└── PhotosXX.iml                   ← IntelliJ project file (auto-generated)

```

## ✨ Features
- Admin can create/delete users
- Users can:
  - Create/delete/rename albums
  - Add/remove photos by file path
  - Caption and tag photos
  - View full-size images and metadata
  - Search by date range or tags
  - Create albums from search results
- Data persists across sessions using serialization
- Fully documented with Javadoc

## 📸 How Photos Work
- Users enter a file path (e.g. `photos/beach.jpg`)
- App loads image from disk using JavaFX `ImageView`
- No image files are copied—only paths are stored

## 📚 Documentation
Run this to generate Javadoc:
```bash
javadoc -d docs src/model/*.java

✅ Test Checklist
Admin
[ ] Log in as admin

[ ] Create new user

[ ] Delete existing user

[ ] Logout and verify changes persist

User
[ ] Log in as created user

[ ] Create album

[ ] Rename album

[ ] Delete album

[ ] Logout and verify albums persist

Album
[ ] Add photo using valid file path

[ ] Remove photo

[ ] Open photo view

Photo
[ ] View full-size image

[ ] Edit caption

[ ] Add tag (e.g. person=maya)

[ ] Remove tag

[ ] View date taken

Search
[ ] Search by date range

[ ] Search by one tag

[ ] Search by two tags with AND/OR

[ ] Create album from search results

Persistence
[ ] Exit app and re-run

[ ] Confirm all users, albums, photos, and tags are saved

🛠 Known Issues
No password support (optional enhancement)

No image preview in album list (optional enhancement)

📌 Notes
All model classes implement Serializable

Data stored in data/users.dat

App uses JavaFX and FXML for UI


---

 Test and Adding screenshots. Then You're ready to submit!
Add the are the problem that you got with this too.
