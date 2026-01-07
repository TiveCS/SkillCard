package com.github.tivecs.skillcard.core.triggers

data class TriggerResult<TAttribute>(val state: TriggerExecuteResultState, val attributes: TAttribute?)
