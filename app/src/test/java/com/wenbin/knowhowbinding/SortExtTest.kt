package com.wenbin.knowhowbinding

import android.util.Log
import android.util.Log.d
import com.wenbin.knowhowbinding.data.User
import com.wenbin.knowhowbinding.ext.excludeOwner
import org.junit.Assert
import org.junit.Test
import com.google.common.truth.Truth.assertThat
import com.wenbin.knowhowbinding.data.source.KnowHowBindingRepository
import com.wenbin.knowhowbinding.ext.recommendedUser
import com.wenbin.knowhowbinding.util.Logger.d
import org.junit.Assert.*
import java.util.logging.Logger


class SortExtTest {

    // Arrange
    private val user1 = User(email = "fake1", talentedSubjects = listOf("英文"), interestedSubjects = listOf("物理"))
    private val user2 = User(email = "fake2", talentedSubjects = listOf("物理"), interestedSubjects = listOf("英文"))
    private val user3 = User(email = "fake3", talentedSubjects = listOf("英文"), interestedSubjects = listOf("數學"))
    private val user4 = User(email = "fake4", talentedSubjects = listOf("數學"), interestedSubjects = listOf("英文"))
    private val user5 = User(email = "owner", talentedSubjects = listOf("物理"), interestedSubjects = listOf("英文"))
    private val ownerUserEmail = "owner"
    private val listOfUsers = listOf<User>(user1, user2, user3, user4, user5)
    private val filteredListOfUsers = listOf<User>(user1, user2, user3, user4)

    // Act & Assert
    @Test
    fun excludeOwnerSuccess() {
        val filteredSize = filteredListOfUsers.size
        val actualList = listOfUsers.excludeOwner(ownerUserEmail)
        val isContainUserOwner = actualList.contains(user5)

        assertThat(filteredSize).isEqualTo(actualList.size)
        assertThat(filteredListOfUsers).isEqualTo(actualList)
        assertFalse(isContainUserOwner)
    }

    @Test
    fun excludeOwnerFail() {
        // Fail means that filteredListOfUsers include ownerUser
        val filteredSize = listOfUsers.size
        val actualList = listOfUsers.excludeOwner(ownerUserEmail)
        assertThat(filteredSize).isNotEqualTo(actualList.size)
        assertThat(listOfUsers).isNotEqualTo(actualList)
    }

    @Test
    fun recommendedUserByTalentedSubjects() {
        val ownerUser = user5
        val originalListUser = listOf<User>(user1, user2, user3, user4, user5)
        val expectedList = listOf<User>(user1)
        val excludedList1 = listOf<User>(user2)
        val excludedList2 = listOf<User>(user4)
        val excludedList3 = listOf<User>(user5)
        val actualList = originalListUser.recommendedUser(ownerUser)
        assertTrue(actualList.containsAll(expectedList))
        assertFalse(actualList.containsAll(excludedList1))
        assertFalse(actualList.containsAll(excludedList2))
        assertFalse(actualList.containsAll(excludedList3))
    }

    @Test
    fun recommendedUserByInterestedSubjects() {
        val ownerUser = user5
        val originalListUser = listOf<User>(user1, user2, user3, user4, user5)
        val expectedList1 = listOf<User>(user1)
        val expectedList3 = listOf<User>(user3)
        val excludedList1 = listOf<User>(user2)
        val excludedList2 = listOf<User>(user4)
        val excludedList3 = listOf<User>(user5)
        val actualList = originalListUser.recommendedUser(ownerUser)
        assertTrue(actualList.containsAll(expectedList1))
        assertTrue(actualList.containsAll(expectedList3))
        assertFalse(actualList.containsAll(excludedList1))
        assertFalse(actualList.containsAll(excludedList2))
        assertFalse(actualList.containsAll(excludedList3))
    }

    @Test
    fun recommendedUserByAll() {
        val ownerUser = user5
        val originalListUser = listOf<User>(user1, user2, user3, user4, user5)
        val expectedList = listOf<User>(user1, user3)
        val excludedList1 = listOf<User>(user2)
        val excludedList2 = listOf<User>(user4)
        val excludedList3 = listOf<User>(user5)
        val actualList = originalListUser.recommendedUser(ownerUser)
        assertEquals(expectedList, actualList)
        assertFalse(actualList.containsAll(excludedList1))
        assertFalse(actualList.containsAll(excludedList2))
        assertFalse(actualList.containsAll(excludedList3))
    }
}