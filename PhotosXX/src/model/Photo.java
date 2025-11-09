public class Photo implements Serializable {
    private String filePath;
    private String caption;
    private LocalDateTime dateTaken;
    private List<Tag> tags;
}
