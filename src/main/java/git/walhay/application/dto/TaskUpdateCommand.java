package git.walhay.application.dto;

public record TaskUpdateCommand(String name, String description, Boolean completed, boolean descriptionProvided) {
	public TaskUpdateCommand(String name, String description, Boolean completed) {
		this(name, description, completed, description != null);
	}
}
