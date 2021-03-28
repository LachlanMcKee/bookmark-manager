package net.lachlanmckee.bookmark.feature.search.service.persistence

import net.lachlanmckee.bookmark.feature.search.service.persistence.dao.SearchDao

interface SearchDatabase {
  fun searchDao(): SearchDao
}
