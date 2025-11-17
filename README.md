# Photos48

**Author:** Esmeralda Bencosme  
**Partner:** Armaan Sleem

A full-featured JavaFX desktop application for photo management with album organization, tagging, search capabilities, and multi-user support. Built with Maven on JDK 21 and JavaFX 21.

## 🎥 Video Demonstration

Watch full demonstrations of Photos48 in action:

**[📺 Complete Application Walkthrough](https://youtu.be/hYDe40m-L7w)** - Overview of all features and functionality

**[📷 How to Add Pictures](https://youtu.be/F_fsyPQudA4)** - Step-by-step guide for importing photos

> **Note:** Due to GitHub's file size limits, the demonstration videos are hosted on YouTube. Click the links above to watch how add picture.

## 📋 Overview

Photos48 is a desktop photo management system that allows users to:
- Organize photos into albums with captions and custom tags
- Import photos with background processing and automatic thumbnail generation
- Search photos by date range or tag combinations (AND/OR logic)
- View photos with interactive zoom controls
- Manage multiple user accounts with isolated workspaces
- Utilize an admin panel for user management

## ✨ Key Features

### Photo Management
- **Album Organization**: Create, rename, and delete albums; photos can exist in multiple albums
- **Smart Import**: Background import with progress tracking and optional copy-to-workspace
- **Thumbnail Cache**: Automatic thumbnail generation with disk caching and invalidation on source updates
- **Photo Metadata**: Captions, tags with custom types, and automatic date/time extraction from file timestamps

### Search & Discovery
- **Date Range Search**: Find photos within specific date ranges (inclusive)
- **Tag Search**: Search by single tag or combine multiple tags with AND/OR logic
- **Case-Insensitive**: All tag searches are case-insensitive for better usability
- **Quick Navigation**: Double-click search results to open photos directly in the viewer

### User Experience
- **Multi-User Support**: Each user has isolated photo storage and album collections
- **Admin Panel**: Create and delete user accounts (accessible via `admin` login)
- **Stock Demo User**: Pre-configured `stock` user with sample images for immediate testing
- **Interactive Viewer**: Zoom in/out with buttons or slider, edit captions and tags on the fly

### Technical Features
- **Persistent Storage**: Java serialization-based data persistence in user home directory
- **Thumbnail Optimization**: Cached thumbnails regenerate only when source files change
- **Graceful Cleanup**: Deleted imported photos moved to `.trash` folder, not permanently removed
- **Cross-Platform**: Works on Windows, macOS, and Linux

## 🖼️ Application Screenshots

### Login Screen
The application starts with a simple login interface. Enter a username to create a new account or access an existing one.

![Login Screen](screenshots/login.png)

### User Home - Album List
View all your albums with photo counts and date ranges. Create, rename, or delete albums from this screen.

![User Home](screenshots/my%20albums.png)

### Album View - Photo Grid
Browse photos in a grid layout with thumbnails. Add, remove, or organize photos with intuitive controls.

![Album View](screenshots/what%20is%20in%20the%20album.png)

### Photo Viewer
View full-size photos with zoom controls, edit captions, manage tags, and navigate between photos.

![Photo Viewer](screenshots/edit%20and%20view.png)

### Search Interface
Search photos by date range or tag combinations. Results display with photo details and can be opened directly.

#### Search by Date Range
![Search by Date Range](screenshots/search%20by%20range.png)

#### Search by Tags
![Search by Tags](screenshots/search%20by%20tag.png)

### Admin Panel
Manage user accounts, create new users, or remove existing ones. Accessible by logging in with username `admin`.

![Admin Panel](screenshots/admin.png)

## 🚀 Getting Started

### Prerequisites
- **JDK 21** or higher
- **Maven 3.9+**

### Installation & Running

1. **Clone or download** the project to your local machine

2. **Navigate** to the project root directory:
   ```powershell
   cd Photos48
   ```

3. **Run the application**:
   ```powershell
   mvn clean javafx:run
   ```
   
   **Note:** Maven will automatically download JavaFX dependencies for your operating system on first run.

4. **Build executable JAR** (optional):
   ```powershell
   mvn clean package
   ```
   Output: `target/Photos48-1.0-SNAPSHOT.jar`

5. **Generate Javadoc** (optional):
   ```powershell
   mvn verify
   ```
   Output: `./docs/index.html`

## 📖 Usage Guide

### First Launch

1. **Launch the application**: `mvn clean javafx:run`

2. **Choose a login option**:
   - **`stock`** - Explore a pre-configured demo account with sample images from `./data`
   - **Any new username** - Create a fresh account with empty album collection
   - **`admin`** - Access admin panel to create/delete user accounts

### Working with Albums

1. From the **User Home** screen, click **Add Album** to create a new album
2. Select an album to view its photos
3. Use **Rename** or **Delete** to manage albums

### Importing Photos

1. Open an album
2. Click **Add Photo** and select image files (jpg, jpeg, png, bmp, gif)
3. A progress dialog shows background import and copy status
4. Thumbnails are automatically generated and cached

### Viewing and Editing Photos

1. Click a photo thumbnail to open the viewer
2. Use **Zoom In/Out** buttons or slider to adjust view
3. Edit the **caption** in the text area
4. **Add tags**: Select tag type from dropdown, enter value, click Add
5. **Delete tags**: Select tag in list, click Delete Tag

### Searching Photos

1. Click **Search** from the User Home screen
2. **Date Range Search**: Select from/to dates, click Search
3. **Tag Search**: 
   - Single tag: Enter one tag value
   - Multiple tags: Enter two tags, select AND or OR mode
4. Double-click results to open photos in viewer

### Testing with Stock Images

The stock user includes sample photos dated **July 23, 2025**:
- Login as `stock`
- Open the "stock" album
- To test search: Use date range 07/23/2025 to 07/23/2025

## 📁 Project Structure

```
Photos48/
├── src/
│   └── main/
│       ├── java/photos48/
│       │   ├── model/                    # Domain Models
│       │   │   ├── Album.java            # Album entity with photo references
│       │   │   ├── Photo.java            # Photo entity with metadata
│       │   │   ├── Tag.java              # Tag entity with type-value pairs
│       │   │   ├── TagType.java          # Tag type definition (person, location, etc.)
│       │   │   └── User.java             # User entity with albums and photo store
│       │   │
│       │   ├── persistence/              # Data Persistence Layer
│       │   │   ├── DataStore.java        # Persistence interface
│       │   │   ├── ObjectDataStore.java  # Java serialization implementation
│       │   │   └── UsersIndex.java       # User index management
│       │   │
│       │   ├── service/                  # Business Logic Layer
│       │   │   ├── AlbumService.java     # Album CRUD operations
│       │   │   ├── AuthService.java      # Authentication logic
│       │   │   ├── PhotoService.java     # Photo management & thumbnails
│       │   │   ├── SearchService.java    # Date and tag search logic
│       │   │   ├── SessionService.java   # User session management
│       │   │   ├── StockService.java     # Stock user initialization
│       │   │   ├── TagService.java       # Tag operations
│       │   │   └── UserService.java      # User CRUD operations
│       │   │
│       │   └── ui/                       # Presentation Layer
│       │       ├── Photos.java           # Main application entry point
│       │       ├── SceneManager.java     # Scene navigation controller
│       │       └── controllers/          # FXML Controllers
│       │           ├── AdminController.java       # Admin panel UI logic
│       │           ├── AlbumController.java       # Album view UI logic
│       │           ├── LoginController.java       # Login screen UI logic
│       │           ├── PhotoViewerController.java # Photo viewer UI logic
│       │           ├── SearchController.java      # Search screen UI logic
│       │           └── UserHomeController.java    # User home UI logic
│       │
│       └── resources/
│           └── fxml/                     # JavaFX FXML Layouts
│               ├── admin.fxml            # Admin panel layout
│               ├── album.fxml            # Album view layout
│               ├── login.fxml            # Login screen layout
│               ├── photo_viewer.fxml     # Photo viewer layout
│               ├── search.fxml           # Search screen layout
│               └── user_home.fxml        # User home layout
│
├── data/                                 # Stock User Sample Images
│   ├── 1.jpg ... 11.jpg                  # Sample photo files
│   └── 𝑩𝒍𝒆𝒂𝒄𝒉.jpg                         # Sample photo file
│
├── screenshots/                          # Application Screenshots
│   ├── admin.png                         # Admin panel screenshot
│   ├── edit and view.png                 # Photo viewer screenshot
│   ├── login.png                         # Login screen screenshot
│   ├── my albums.png                     # User home screenshot
│   ├── search by range.png               # Date search screenshot
│   ├── search by tag.png                 # Tag search screenshot
│   └── what is in the album.png          # Album view screenshot
│
├── docs/                                 # Generated Javadoc
│   └── index.html                        # Javadoc entry point
│
├── target/                               # Maven Build Output
│   ├── classes/                          # Compiled .class files
│   ├── Photos48-1.0-SNAPSHOT.jar         # Built JAR file
│   └── ...                               # Other build artifacts
│
├── pom.xml                               # Maven Project Configuration
├── README.md                             # This documentation
├── IMPLEMENTATION_SUMMARY.md             # Implementation details
└── TESTING_GUIDE.md                      # Testing instructions
```

### Architecture Overview

**Model Layer** (`model/`)
- Pure Java domain objects implementing `Serializable`
- `User` → `Album` → `Photo` → `Tag` relationships
- Photo store is centralized in User (photos can exist in multiple albums)

**Persistence Layer** (`persistence/`)
- `DataStore` interface for storage abstraction
- `ObjectDataStore` uses Java serialization to `~/.photos48/`
- Each user stored in separate `.ser` file

**Service Layer** (`service/`)
- Business logic isolated from UI concerns
- Services handle CRUD operations, search, authentication, thumbnails
- `StockService` initializes demo user on first launch

**Presentation Layer** (`ui/`)
- JavaFX application with FXML-based views
- `SceneManager` handles navigation between screens
- Controllers bind FXML components to service layer logic
- Thumbnail caching with automatic invalidation

## 💾 Data Storage

- **User Data**: `~/.photos48/` (Windows: `C:\Users\<username>\.photos48\`)
  - `users.ser` - List of all usernames
  - `user_<username>.ser` - Per-user data (albums, photos, tags)
- **Thumbnails**: `~/.photos48/.thumbnails/user_<username>/`
- **Trash**: `~/.photos48/.trash/user_<username>/` (deleted imported photos)
- **Stock Images**: `./data/` (source images for stock user initialization)

## 🛠️ Technical Details

### Architecture
- **Model-View-Controller (MVC)**: Clear separation of concerns
- **Service Layer**: Business logic isolated from UI controllers
- **Persistence Layer**: Java serialization with DataStore interface

### Technologies
- **JavaFX 21**: Modern desktop UI framework
- **Maven**: Dependency management and build automation
- **Java Serialization**: Simple persistence with versioned compatibility

### Main Class
- Entry point: `photos48.ui.Photos`

## 📝 Notes

- Photos can appear in multiple albums simultaneously (reference-based)
- Imported photos are optionally copied to user workspace for portability
- Thumbnails automatically regenerate when source files are modified
- Admin account cannot be deleted to prevent lockout
- All tag searches are case-insensitive for better user experience

## ✅ Compliance & Requirements

This project strictly adheres to the following development requirements:

### Standard Java Only
- **JDK 21** - Uses only standard Java installation
- **No External Vendor Libraries** - No third-party dependencies like Apache Commons, Google Guava, or Lombok
- **Pure Java Implementation** - All business logic uses standard Java libraries (`java.io`, `java.nio`, `java.time`, `java.util`)

### GUI Framework
- **JavaFX 21 Only** - Modern Java GUI framework
- **FXML Layouts** - All UI screens designed with FXML (6 layout files)
- **No Swing** - Zero `javax.swing` imports or Swing components
- **Maven JavaFX Plugin** - Automatically manages JavaFX dependencies

### Documentation
- **Complete Javadoc** - Every class documented with Javadoc comments
- **Authorship Tags** - All 24 classes include `@author Esmeralda Bencosme`
- **Version Tags** - All classes include `@version 1.0`
- **Method Documentation** - Parameters, return values, and exceptions documented with `@param`, `@return`, `@throws` tags

### Portability
- **Cross-Platform** - Runs on Windows, macOS, and Linux
- **Maven Build System** - Standard build process with `mvn javafx:run`
- **No Hardcoded Paths** - Uses system properties for user home directory
- **Self-Contained** - All dependencies managed through Maven Central

**Testing Assurance:** This application will run with standard Java JDK 21 and Maven without requiring any external packages or manual library installation.

## 📄 License

This project is developed as an academic assignment.

---

**Photos48** - Simple, photo management for desktop.
