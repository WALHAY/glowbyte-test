package git.walhay.presentation.dto;

import git.walhay.application.dto.TaskUpdateCommand;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record TaskUpdateDto(
		@Pattern(regexp = "(?s).*[^\\p{javaWhitespace}].*", message = "must not be blank") @Size(max = 255) String name,

		String description,
		Boolean completed) {
	public TaskUpdateCommand toCommand() {
		return new TaskUpdateCommand(name, description(), completed());
	}
}
