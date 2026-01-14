package com.github.tivecs.skillcard.core.triggers

data class TriggerResult<TAttribute : TriggerAttribute>(val state: TriggerExecuteResultState, val attributes: TAttribute?)