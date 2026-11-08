package git.walhay.presentation.dto;

import git.walhay.application.dto.TaskCreateCommand;
import jakarta.annotation.Nonnull;

public record TaskCreateDto(@Nonnull String name, @Nonnull String description) {
	public TaskCreateCommand toCommand() {
		return new TaskCreateCommand(name, description);
	}
}
