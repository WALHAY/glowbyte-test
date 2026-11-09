package git.walhay.presentation.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import git.walhay.application.dto.TaskUpdateCommand;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class TaskUpdateDto {
	@Pattern(regexp = "(?s).*[^\\p{javaWhitespace}].*", message = "must not be blank")
	@Size(max = 255) private String name;

	private String description;
	private Boolean completed;
	private boolean descriptionProvided;

	public void setName(String name) {
		this.name = name;
	}

	@JsonSetter("description")
	public void setDescription(String description) {
		this.description = description;
		this.descriptionProvided = true;
	}

	public void setCompleted(Boolean completed) {
		this.completed = completed;
	}

	public TaskUpdateCommand toCommand() {
		return new TaskUpdateCommand(name, description, completed, descriptionProvided);
	}
}
