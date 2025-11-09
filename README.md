Assignment 3
Photos
Posted Thu Oct 23
Due Mon Nov 17, 11 PM in GitHub

First individual GitHub commit by Fri, Oct 31, 11 PM

Worth 200 points (20% of course grade)
Git/GitHub How-To
For this assignment you will build a single-user photo application that allows storage and management of photos in one or more albums.
You will continue working with your partner.

All user interaction must be implemented in Java FX, and all UIs--except for standard Java FX dialogs such as Alert and TextInputDialog--must be designed in FXML.

You may use multiple stages to show complex secondary windows, and switch multiple scenes within a stage.

IMPORTANT: You will be maintaining your code in a repository on GitHub. Learning how to manage your source code on Gitbub INCREMENTALLY and in COLLABORATION with your project partner is an important real-word skill, and you want to make the most of this opportunity to learn this skill.

Use JDK 21 (Java version 21) and Java FX SDK 21 to build your program. The same versions will be used to grade your program. Using other versions of the JDK or Java FX SDK will create issues that might impact your grade.

Contents
Features
Model
Complete Implementation
Submission/Code Maintenance in GitHub
Grading
FAQs
Features
Your application must implement the following features:

Date of photo

Since we won't examine the contents of a photo file to get the date the photo was taken, we will instead use the last modification date of the photo file (as provided via the Java API to the filesystem) as a proxy. (The user interface will still refer to this as the date the photo was taken.)

To store and manipulate dates and times, you have two options:

You can use a java.util.Calendar instance.
In which case, when you set a date and time on an instance, also make sure you set milliseconds to zero, as in cal.set(Calendar.MILLISECOND,0), otherwise your equality checks won't work correctly.
Alternatively, you may use the classes in the java.time package.

Tags

Photos can be tagged with pretty much any attribute you think is useful to search on, or group by. Examples are location where photo was taken, and names of people in a photo, so you can search for photos by location and/or names.

From the implementation point of view, it may be useful to think of a tag as a combination of tag name and tag value, e.g. ("location","New Brunswick"), or ("person","susan"). A photo may have multiple tags (name+value pairs), but no two tags for the same photo will have the same name and value combination.

Additional details:

You can set up some tag types beforehand for the user to pick from (e.g. location)
Depending on the tag type, a user can either have a single value for it, or multiple values (e.g. for any photo, location can only have one value, but if there's a person tag, that can have multiple values, one per person that appears in the photo)
A user can define their own tag type and add it to the list (so from that point on, that tag type will show up in the preset list of types the user can choose from)
Location of Photos - Stock photos and User photos

There are two sets of photos, stock photos that come pre-loaded with the application, and user photos that are loaded/imported by a user when they run the application.

Stock photos are photos that you will keep in the application's workspace. You must have no fewer than 5 stock photos, and no more than 10.
Create a special username called "stock" (no password, or password="stock") and store the stock photos under this user, in an album named "stock".

Leave the photos in the application's workspace so the graders can test your application starting with your stock photos, then load other photos from their computer, see "User photos" below.

Try to work with low/medium resolution pictures for the stock photos because they will be on GitHub and downloaded by the graders, and you don't want to bloat your project size.

User photos are photos that your application can allow a user to load from their computer, so they can be housed anywhere on the user's machine. The actual photos must NOT be in your application's workspace. Instead, your application should only store the location of the photo on the user's machine. User photo information must NOT be in the released project in GitHub since each installation of your application on a machine will have its own set of users.
Login

When the application starts, a user logs in with username. Password implementation is optional. It makes for a "real" scenario, but is irrelevant to the essence of the project. (There is no credit for the password feature, if you choose to implement it.)
Admin Subsystem
There must be a special username admin that will put the application in an administration sub-system. The admin user can then do any of the following:
List users
Create a new user
Delete an existing user
Note: If you elect to implement passwords for users, make "admin" the password for the admin user, so it's easier to grade. Otherwise we will need to ask you, or look in some README file, etc, which just turns out to be a needless hassle.

Non-admin User Subsystem

Once the user logs in successfully, all albums and photo information for this user from a previous session (if any) are loaded from disk.
Initially, all the albums belonging to the user should be displayed. For each album, its name, the number of photos in it, and the range of dates (earliest and latest date) on which photos were taken must be displayed. Use your discretion on how to show this additional information.

The user can then do the following:
Create albums
Delete albums
Rename albums
Open an album. Opening an album displays all photos, with their thumbnail images and captions, inside that album. Once an album is open the user can do the following:
Add a photo
Remove a photo
Caption/recaption a photo
Display a photo in a separate display area. The photo display should also show its caption, its date-time of capture (see Date of photo below), and all its tags (see Tags below).
Add a tag to a photo
Delete a tag from a photo
Copy a photo from one album to another (multiple albums may have copies of the same photo)
Note: If a photo is in multiple albums, it is the same physical photo, just referenced/contained in multiple albums. This means any changes you make to the photo (caption, tags) will be reflected in all the albums in which the photo appears.

Move a photo from one album (source) to another (the photo will be removed from the source album)
Go through photos in an album in sequence forward or backward, one at a time, with user interaction (manual slideshow)
Search for photos (Photos that match the search criteria should be displayed in a similar way to how photos in an album are displayed). Under this, you should provide the following specific features:
Search for photos by a date range.
Search for photos by tag type-value pairs. The following types of tag-based searches should be implemented:
A single tag-value pair, e.g person=sesh
Conjunctive combination of two tag-value pairs, e.g. person=sesh AND location=prague
Disjunctive combination of two tag-value pairs, e.g. person=sesh OR location=prague
For conjunctions and disjunctions, if a tag can have multiple values for a photo, it can appear on both arms of the conjunction/disjunction, e.g. person=andre OR person=maya, person=andre AND person=maya
You are NOT required to do conjunctions/disjunctions on more than two tag-values pairs.
In other words, you are not required to do stuff like t1=v1 and t2=v3 and t3=v3
There should be functionality to create an album containing the search results.
As mentioned earlier (under Copy a photo from one album to another), a photo can be in multiple albums. Creating an album out of search results means copying these photos to a new album, without deleting them from the current album(s) to which they belong.

Note: A single user may not have duplicate album names, but an album name may be (coincidentally) duplicated across users.

Logout

The user (whether admin or non-admin) logs out at the end of the session. All updates made by the user are saved to disk.
After a user logs out, the application is still running, allowing another user to log in.
Quit Application

There should be a way for the user to quit the application safely at any time, bypassing the logout step (e.g. by killing the main window). Safely means that all updates that were made in the application in the user's session are saved on disk.
Unlike logout, the application stops running. The next user that wants to use the application will need to restart it.
Errors

In the application all errors and exceptions should be handled gracefully within the GUI setup. The text console should NOT be used at all: not to report any error, not to read input, not to print output.
Model
The model should include all data objects, plus code to store and retrieve photos for a user. The collection of classes that comprise the model should be in its own package, separate from the view and controller.

You are required to use the java.io.Serializable interface, and the java.io.ObjectOutputStream/java.io.ObjectInputStream classes to store and retrieve data.

See Notes on Serialization and Versioning to know how to implement serialization and deserialization.

Note that your application will need to store content for multiple users, so it would be a good idea to separate different user's contents from each other.

You need to think about what objects you want to have in your design, with what attributes and operations. It is important to plan this out and come up with a good object-oriented design that clearly separates roles and functions between objects, and can be cleanly extended to add more features for future versions of the application.

Implementation
Code your application using the standard installation of Java, using for your GUI Java FX and FXML only (No Swing). No external vendor libraries. We will test with standard Java so if you use any external packages, your program will not run, and you will not get credit.

Document every class you implement with Javadoc tags, and be sure to include authorship.

Submission/Code Maintenance in GitHub
Use the Git/GitHub How-To to know how create a repository in GitHub and manage it using Git. There is a comprehensive walk through of all the features you need to know to manage your code collaboratively. In particular, all Git examples are shown on the command line, which is the recommended way to use Git from your computer. This is because it is clear as to what's going on, so it is easy to recover from mistakes, if any. (Using an app/plugin with a GUI generally hides a lot of things under the hood, and if things go awry you may not have enough transparent info to work with and fix things.)

Create a new private repository, and add as collaborators your partner and grader. You may add your grader as collaborator at any time, there is no requirement that you do it immediately. However, you must do it by the time the assignment is due, otherwise we will assess a penalty.

Create a Java project, name it PhotosXX, where XX is your two digit group number. Use packages as necessary.

Create directories docs and data directly under the project, NOT under src or under any of the packages.

The complete Javadoc HTML documentation should be generated and placed in the docs directory.
Stock photos for the stock username should be in the data directory.
There should one class called Photos that should have the main method so it can be launched as an application.
EACH partner in the team will make a first GitHub commit in the project latest by Friday, October 31, 11 PM.
The GitHub commit does not have to be any Java code, you may commit any text file, with as little as a single letter in it.

Thereafter, you will make commits incrementally, as and when you add reasonable functionality to your implementation. Aside from that first commit that should come from each partner, we are not asking for a specific number of commits from either partner, as long as you have found a way to work together.

In any case, do NOT use GitHub like a Canvas assignment drop, as in not making any commits after the initial required commit, then making the second one the final commit just before the deadline. If you do this, you are wasting a great opportunity to learn an important skill you will be required to use in real projects, and will have one less thing to show to prospective employers.

Grading
Your project will be graded on the following, for 200 points:
Category	Points
Features	190
Javadoc in-code comments and
generated HTML documentation pages	10

Penalties (up to 25 points total) will be assessed on the following:

No initial GitHub commit by Oct 31 (individual)
Read access to repository not given to grader by the due date
Not using FXML adequately/appropriately to design the UI
Inadequate Javadoc tags/Javadoc HTML documentation not generated
Project structure does not properly separate model, view, control classes with appropriate package configuration
Lacks scalability i.e. doesn't display large amounts of data (e.g. many tens of photos or more) in a easily navigable way
Lateness penalties (separate from penalties above):

10 pts: Every time you ask us and we test another commit version in your repository that is earlier than the last commit before the deadline.
10 pts: For every 2 hours of lateness, in case there is nothing in the repository for us to test as of the deadline of Nov 17, 11 PM.
NOTE: This 2 hour block will be applied STRICTLY starting any time after 11 PM (even if it is one second), in increments of 2 hours. NO EXCEPTIONS.
Frequently Asked Questions
Q: Are search criteria disjoint, user can search photo for EITHER date range or tag type-value pairs or can they search for a tag type-value pair within a certain date range?
A: Date range and tag-value pairs are disjoint for searches. So either date range or tag-value pairs, but not together.

Q: What photo formats are allowed?
A: BMP, GIF, JPEG, PNG. See https://www.tutorialspoint.com/how-to-display-an-image-in-javafx

Q: Do we need to show standard dialogs (Alert, TextInputDialog) in our storyboard?
A: You need not show error/confirmation dialogs. But you must show any dialog that is getting user input, whether it is a standard dialog (boilerplate JavaFX), or a dialog you designed with FXML.

Q: Should we preserve the aspect ratio of the image the user views?
A: Not a requirement (i.e. you won't lose credit if you don't), but preserving the aspect ratio is encouraged.

Q: Does admin also have albums and everything else, like non-admin user? Or just edit/create/delete user?
A: No. admin can only create/delete user, and list all users.

Q: If we have two tags like "person": "Alice" and "person": "Bob". Should we store them in two different tags? Or we should do it in a single tag "person":"Alice; Bob"? Or both are fine?
A: You should store them in two different tags, one per person.

Q: Do we need to display photos/users/albums in sorted order?
A: No.

Q: Is a user allowed to have duplicates of the same picture in the same album?
A: No.

Q: Can a user edit the date of a photo?
A: No. The date of the photo is basically the date of the photo file, it can't be modified in the app

Q: If you have already added a photo to one album, can you add it to another album (via importing the photo again) not copying or moving the photo?
A: Yes. But it is equivalent to copying. And be sure to read the note in the assignment description (in red) on copying a photo from one album to another since the same rules apply in this situation.

Q: Are we allowed to have duplicate users?
A: No.

Q: Can there be photos that are not in an album?
A: No, every photo must be in at least one album.

Q: When we delete an album are we also deleting all references to the photos that were in the now deleted album?
A: When you delete an album, all the references to photos from that album will be deleted. But if a photo is in a different album as well, that album will still refer to the photo.

Q: Should stock be auto-generated every time the program is run, in case admin deletes the "stock" user?
A: No, you don't need to have functionality to recreate stock if deleted.
