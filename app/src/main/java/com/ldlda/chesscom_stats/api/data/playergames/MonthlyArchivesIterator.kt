package com.ldlda.chesscom_stats.api.data.playergames

class MonthlyArchivesIterator(monthlyArchives: MonthlyArchives) : Iterator<MonthlyArchive> {
    val monthlyArchivesArchivesIterator = monthlyArchives.archives.iterator()
    override fun next(): MonthlyArchive {
        monthlyArchivesArchivesIterator.next()
        TODO("how do i even do ts right bro")
    }

    override fun hasNext(): Boolean =
        monthlyArchivesArchivesIterator.hasNext()
}