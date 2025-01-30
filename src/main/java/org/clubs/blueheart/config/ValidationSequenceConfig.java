package org.clubs.blueheart.config;

import jakarta.validation.GroupSequence;

@GroupSequence({ValidationGroups.NotNullGroup.class, ValidationGroups.NotBlankGroup.class, ValidationGroups.PatternGroup.class, ValidationGroups.SizeGroup.class, ValidationGroups.DateTimeGroup.class})
public interface ValidationSequenceConfig {
}

