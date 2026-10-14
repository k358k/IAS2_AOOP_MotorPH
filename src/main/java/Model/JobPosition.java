package Model;

/**
 * @author Gracielle Salvador
 */

/**
 * Represents a job position with a title and description.
 */
public class JobPosition {
    private Integer positionId;
    private String title;
    private String description;

    // Default constructor
    public JobPosition() {
    }

    // Parameterized constructor
    public JobPosition(String title, String description) {
        this.title = title;
        this.description = description;
    }

    // Getters and Setters
    public Integer getPositionId() {
        return positionId;
    }

    public void setPositionId(Integer positionId) {
        this.positionId = positionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "JobPosition{" +
                "positionId=" + positionId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
