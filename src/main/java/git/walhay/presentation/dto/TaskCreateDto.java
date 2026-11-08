package git.walhay.presentation.dto;

import git.walhay.application.dto.TaskCreateCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TaskCreateDto(@NotBlank @Size(max = 255) String name, String description) {
	public TaskCreateCommand toCommand() {
		return new TaskCreateCommand(name, description);
	}
}
