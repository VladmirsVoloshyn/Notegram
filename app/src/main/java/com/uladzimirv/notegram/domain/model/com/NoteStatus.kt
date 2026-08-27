package com.uladzimirv.notegram.domain.model.com

sealed class NoteStatus(val formal: FormalStatus) {

    data class Deleted(
        val deletedAt: Long,
        val formalStatus: FormalStatus = FormalStatus.DELETED
    ) : NoteStatus(formalStatus)

    data class Archived(
        val archivedAt: Long,
        val formalStatus: FormalStatus = FormalStatus.ARCHIVED
    ) : NoteStatus(formalStatus)

    data class None(
        val formalStatus: FormalStatus = FormalStatus.NONE
    ) : NoteStatus(formalStatus)


}