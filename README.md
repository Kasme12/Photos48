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

PhotosXX/ ├── src/ │ 
            ├── model/ ← Data classes + Photos.java │
            ├── controller/ ← JavaFX controllers │
            ├── view/ ← FXML layout files
            ├── data/ ← Serialized user data (users.dat) 
            ├── docs/ ← Javadoc HTML output 
            ├── README.md

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
