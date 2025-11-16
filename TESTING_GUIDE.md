# Photos48 - Complete Testing Guide

## Getting Started

### Prerequisites
- JDK 21 installed (at C:\jdk-21_windows-x64_bin\jdk-21.0.9)
- JavaFX SDK 21 (at C:\Users\edwar\Downloads\openjfx-21.0.9_windows-x64_bin-sdk\javafx-sdk-21.0.9)
- Maven 3.x installed
- Windows PowerShell or Command Prompt

### Starting the Application

```powershell
cd C:\Users\edwar\Downloads\Photo48
mvn exec:java
```

The application window will open showing the login screen.

## Testing Scenarios

### 1. Basic User Login Flow

**Test Case 1.1: Create New User**
```
Action: Login screen appears
Step 1: Enter "john_doe" in username field
Step 2: Click "Login / Create"
Expected: User created, navigates to User Home screen showing "Photos48 - john_doe"
Verify: User home has empty album list
```

**Test Case 1.2: Login Existing User**
```
Action: Continue from 1.1, click Logout
Step 1: Enter "john_doe" again
Step 2: Click "Login / Create"
Expected: Logs in to existing user (no new user created)
Verify: Navigates to User Home screen
```

**Test Case 1.3: Admin Login**
```
Step 1: From login screen, enter "admin"
Step 2: Click "Login / Create"
Expected: Admin panel opens instead of User Home
Verify: Admin panel shows TableView with username column
```

### 2. Admin Panel Testing

**Test Case 2.1: Create User from Admin**
```
Prerequisites: Logged in as admin
Step 1: See text field labeled with username input
Step 2: Enter "alice" and click "Create User" button
Step 3: Observe table update
Expected: "alice" appears in user list TableView
Verify: No error alert shown
```

**Test Case 2.2: Delete User from Admin**
```
Prerequisites: Admin panel with at least 2 users
Step 1: Click on "alice" in the table (select row)
Step 2: Click "Delete User" button
Expected: Confirmation dialog appears
Step 3: Click OK
Expected: "alice" disappears from table, success message shown
```

**Test Case 2.3: Prevent Admin Deletion**
```
Prerequisites: Admin panel
Step 1: Click on "admin" in table
Step 2: Click "Delete User"
Expected: Error message "Cannot delete admin user"
Verify: admin still in table
```

**Test Case 2.4: Logout from Admin**
```
Prerequisites: Admin panel
Step 1: Click "Logout" button
Expected: Confirmation dialog
Step 2: Click OK
Expected: Returns to login screen
```

### 3. User Home (Album Management)

**Test Case 3.1: Create Album**
```
Prerequisites: Regular user logged in (e.g., "john_doe")
Step 1: Click "Add Album" button
Expected: Text input dialog appears "Enter album name:"
Step 2: Enter "Vacation 2024"
Step 3: Click OK
Expected: Album list updates, "Vacation 2024" appears
Verify: Details panel shows: Album: Vacation 2024, Photos: 0, Date range: (no photos)
```

**Test Case 3.2: Select Album Shows Details**
```
Prerequisites: Album exists (e.g., "Vacation 2024")
Step 1: Click "Vacation 2024" in album list
Expected: Details panel shows with correct name and count
Verify: Details panel is visible (setVisible=true)
```

**Test Case 3.3: Create Multiple Albums**
```
Prerequisites: User home with one album
Step 1: Add another album "Family"
Step 2: Add third album "Work"
Expected: All three appear in list
Verify: Can select each and see details update
```

**Test Case 3.4: Delete Album**
```
Prerequisites: Album "Work" selected
Step 1: Click "Delete Album"
Expected: Confirmation dialog
Step 2: Click OK
Expected: "Work" removed from list, success message shown
Verify: Other albums remain
```

**Test Case 3.5: View Album (Enter Album View)**
```
Prerequisites: "Vacation 2024" album selected
Step 1: Click "View Album" button
Expected: Album view screen opens
Verify: Title shows "Photos48 - Album: Vacation 2024"
Verify: Empty FlowPane (no photos yet)
```

### 4. Album View (Photo Management)

**Test Case 4.1: Add Photo**
```
Prerequisites: Album view open (empty album)
Step 1: Click "Add Photo" button
Expected: FileChooser dialog opens
Step 2: Navigate to any image file (jpg, png, gif, bmp)
Step 3: Select file and click Open
Expected: Photo button appears in FlowPane with file name
Verify: Success alert "Photo added successfully"
```

**Test Case 4.2: Add Multiple Photos**
```
Prerequisites: Album with one photo
Step 1: Click "Add Photo" again
Step 2: Select different image file
Expected: Second photo button appears
Step 3: Repeat to add 3-5 photos total
Verify: FlowPane grid fills with photo buttons
```

**Test Case 4.3: Select and View Photo**
```
Prerequisites: Album with photos
Step 1: Click on a photo button in grid
Expected: Button highlights with blue border
Step 2: Click "View Photo" button
Expected: Photo viewer screen opens with image displayed
```

**Test Case 4.4: Remove Photo**
```
Prerequisites: Album with photos, one selected (blue border)
Step 1: Click "Remove Photo"
Expected: Confirmation dialog
Step 2: Click OK
Expected: Photo removed from grid, grid updates
```

**Test Case 4.5: Copy Photo to Another Album**
```
Prerequisites: Two albums with photos
Step 1: Go to Album A, select a photo
Step 2: Click "Copy Photo"
Expected: ChoiceDialog with album list
Step 3: Select Album B
Expected: Success message "Photo copied"
Step 4: Navigate to Album B
Verify: Photo appears in Album B as well
Verify: Original still in Album A
```

**Test Case 4.6: Move Photo Between Albums**
```
Prerequisites: Album A with photos, Album B exists
Step 1: Select photo in Album A
Step 2: Click "Move Photo"
Expected: ChoiceDialog
Step 3: Select Album B
Expected: Photo disappears from Album A grid
Step 4: Navigate to Album B
Verify: Photo now only in Album B
```

### 5. Photo Viewer (Tagging)

**Test Case 5.1: View Photo Details**
```
Prerequisites: Photo viewer open with image loaded
Verify: ImageView shows actual photo
Verify: Caption TextArea is editable (empty or has existing caption)
Verify: DateTime label shows photo date
Verify: Tags ListView exists (initially empty)
```

**Test Case 5.2: Edit Caption**
```
Prerequisites: Photo viewer open
Step 1: Click in TextArea caption field
Step 2: Type "Beautiful sunset at the beach"
Step 3: Click "Update Caption" button
Expected: Success alert "Caption updated"
Verify: TextArea retains text
Verify: If you navigate away and back, caption persists
```

**Test Case 5.3: Add Single-Type Tag**
```
Prerequisites: Photo viewer, "location" tag visible in ComboBox
Step 1: Tag Type ComboBox shows "location" (SINGLE type)
Step 2: Enter "Beach" in tag value field
Step 3: Click "Add Tag"
Expected: Tag appears in ListView as "location: Beach"
Success alert shown
```

**Test Case 5.4: Add Multi-Type Tag**
```
Prerequisites: Photo viewer
Step 1: Change ComboBox to "person" (MULTI type)
Step 2: Enter "John" in value field
Step 3: Click "Add Tag"
Expected: "person: John" appears in ListView
Step 4: Add another "person: Alice"
Expected: Both tags appear (MULTI allows multiple same-type)
```

**Test Case 5.5: Enforce Tag Multiplicity (SINGLE)**
```
Prerequisites: Photo with "location: Beach" tag
Step 1: Try to add another location tag "location: Mountain"
Step 2: Click "Add Tag"
Expected: First location tag replaced (still only "location: Mountain")
Verify: ListView shows only one location tag
```

**Test Case 5.6: Remove Tag**
```
Prerequisites: Photo with tags
Step 1: Click on a tag in ListView to select
Step 2: Click "Remove Tag"
Expected: Tag disappears from list, success message shown
Verify: Other tags remain
```

**Test Case 5.7: Navigate Back**
```
Prerequisites: Photo viewer
Step 1: Click "Back" button
Expected: Returns to User Home screen
Verify: All previous data (albums, photos) still visible
```

### 6. Search Functionality

**Test Case 6.1: Search by Date Range**
```
Prerequisites: User with photos that have dates
Step 1: Go to Search screen (from Album view → Search button)
Step 2: Select "From Date" as 01/01/2024
Step 3: Select "To Date" as 12/31/2024
Step 4: Click "Search by Date"
Expected: Results list shows matching photos
Expected: Info alert shows count
Verify: Photos with dates in range appear
```

**Test Case 6.2: Search by Single Tag**
```
Prerequisites: Photos with tags (e.g., "location: Beach")
Step 1: Search mode: "Single"
Step 2: Tag type: "location"
Step 3: Tag value: "Beach"
Step 4: Click "Search by Tags"
Expected: Only photos with location:Beach tag appear
```

**Test Case 6.3: Search with AND**
```
Prerequisites: Photos with multiple tags
Step 1: Search mode: "AND"
Step 2: Tag 1: type="location", value="Beach"
Step 3: Tag 2: type="person", value="John"
Step 4: Click "Search by Tags"
Expected: Only photos with BOTH tags appear
```

**Test Case 6.4: Search with OR**
```
Prerequisites: Photos with different tags
Step 1: Search mode: "OR"
Step 2: Tag 1: "location" "Beach"
Step 3: Tag 2: "location" "Mountain"
Step 4: Click "Search by Tags"
Expected: Photos with either location appear
```

**Test Case 6.5: Create Album from Search Results**
```
Prerequisites: Search results visible
Step 1: Click "Create Album from Results"
Expected: Text input dialog "Album name:"
Step 2: Enter "Beaches"
Step 3: Click OK
Expected: New album created with all result photos
Verify: Navigate to User Home, see new "Beaches" album
Verify: Enter album, see all search result photos
```

### 7. Data Persistence

**Test Case 7.1: Data Saves on Logout**
```
Prerequisites: User with albums and photos with tags
Step 1: Go to User Home
Step 2: Click "Logout"
Expected: User data saved
Verify: Success logout to login screen
```

**Test Case 7.2: Data Persists After Relogin**
```
Prerequisites: Just logged out with data
Step 1: Login with same username
Expected: All albums appear in list
Step 2: Enter an album
Verify: All photos still there with captions and tags
```

**Test Case 7.3: Multi-User Data Isolation**
```
Prerequisites: Two users logged in separately (previously)
Step 1: Login as User A
Expected: See User A's albums and photos
Step 2: Logout, login as User B
Expected: See User B's albums and photos (not User A's)
Verify: Data is completely isolated
```

**Test Case 7.4: Stock User**
```
Prerequisites: Fresh app start
Step 1: Login as "stock"
Expected: User home appears with "stock" album
Verify: If data/stock/ had images, they'd appear in album
```

### 8. Integration Test - Full Workflow

```
Complete workflow test:

1. Start app, login as new user "photographer"
2. Create 2 albums: "Nature" and "City"
3. Add 3 photos to Nature album
4. Add 2 photos to City album
5. Tag nature photos: location: Forest, person: Sarah
6. Tag city photos: location: Downtown, person: Mike
7. Logout, verify data saved
8. Login as photographer again
9. Verify all albums and tags persist
10. Search by location: Forest
11. Create album from results: "ForestShots"
12. Verify ForestShots contains forest photos
13. Copy a city photo to Nature album
14. Edit captions on various photos
15. Logout, login as admin
16. Create another user "viewer"
17. Logout, verify photographer's data unchanged
18. Login as photographer, all data intact
```

## Troubleshooting

### Application Won't Start
- Ensure Maven is installed and in PATH
- Check Java 21 is in PATH
- Try: `java -version` (should show 21.x.x)

### JavaFX Window Doesn't Appear
- The warning about "Unsupported JavaFX configuration" is normal with modules
- Window should still appear despite warning
- Check if window is off-screen

### File Chooser Not Working
- Ensure you're selecting valid image files (jpg, png, gif, bmp)
- Try different file locations
- Ensure file path is accessible

### Data Not Saving
- Check if ~/.photos48/ directory exists and is writable
- Try restarting application
- Check for errors in terminal output

### Tags Not Enforcing Multiplicity
- Location type should only allow one tag
- Person type should allow multiple
- Try adding second location tag - first should be replaced

## Success Criteria

✅ Application compiles without errors
✅ All 6 screens navigate correctly
✅ User data persists across sessions
✅ Admin can manage users
✅ Users can create/delete albums
✅ Photos can be added/removed
✅ Tags work with multiplicity rules
✅ Search finds correct photos
✅ Albums can be created from search results

---

**Last Updated**: November 15, 2025
**Version**: 1.0 - Complete Implementation
