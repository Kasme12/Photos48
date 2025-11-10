How to Run and Test Your App
✅ 1. Open Your Project in IntelliJ
Make sure your folder structure looks like this:

PhotosXX/
├── src/
│   ├── model/
│   ├── controller/
│   ├── view/
│   └── model/Photos.java  ← main class
├── data/                  ← created automatically
├── docs/                  ← generated manually

✅ 2. Set Up IntelliJ to Run Photos.java

In IntelliJ:
Right-click Photos.java in src/model
Select Run 'Photos.main()'

This launches your app and shows the login screen.

✅ 3. What Happens When You Run It
Your Photos.java does three things:
public class Photos extends Application {
    public void start(Stage primaryStage) {
        PhotoManager.loadData();  // Load users from data/users.dat
        // Show login.fxml
    }

   \\ public void stop() {
        PhotoManager.saveData();  // Save users to data/users.dat
    }

   \\ public static void main(String[] args) {
        launch(args);  // Start JavaFX app
    }
\\}

How the App Works Step-by-Step
🔹 1. Login Screen (login.fxml)
Enter a username

If admin, go to admin.fxml

If regular user, go to user.fxml

🔹 2. Admin Dashboard (admin.fxml)
Create/delete users

Stored in PhotoManager.getUsers()

🔹 3. User Dashboard (user.fxml)
Create/delete/rename albums

Each album is a list of Photo objects

🔹 4. Album View (album.fxml)
Add/remove photos by file path

Open photo to view full-size

🔹 5. Photo View (photo.fxml)
Edit caption

Add/remove tags

View date taken

🔹 6. Search View (search.fxml)
Search by date or tags

Create new album from results

✅ How to Generate Javadoc
Run this in your terminal (from project root):

javadoc -d docs src/model/*.java

This creates HTML documentation in the docs/ folder.

✅ How to Test Functionality
Try these flows:

Log in as admin → create users

Log in as a user → create albums

Add photos using paths like photos/beach.jpg

Open photo → edit caption, add tags

Search by date or tags → create album from results

Exit app → re-run → confirm data persists

✅ How Login Works in Your App
🔹 Admin Login
Username: admin

Password: none required

This opens the admin.fxml screen for managing users.

🔹 Regular User Login
Username: any user created by the admin

Password: none required

This opens the user.fxml screen for managing albums and photos.

How to Test It
Run the app → login screen appears

Type admin → click login → admin dashboard opens

Create a user (e.g. esme)

Log out → type esme → click login → user dashboard opens

