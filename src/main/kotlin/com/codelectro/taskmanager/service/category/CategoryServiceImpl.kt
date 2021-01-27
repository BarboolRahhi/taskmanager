package com.codelectro.taskmanager.service.category

import com.codelectro.taskmanager.dto.CategoryDto
import com.codelectro.taskmanager.dto.MessageResponse
import com.codelectro.taskmanager.exception.NotFoundException
import com.codelectro.taskmanager.exception.UnauthorizedException
import com.codelectro.taskmanager.repository.CategoryRepository
import com.codelectro.taskmanager.repository.UserRepository
import com.codelectro.taskmanager.service.AuthService
import com.codelectro.taskmanager.service.toCategory
import com.codelectro.taskmanager.service.toCategoryDto
import com.codelectro.taskmanager.service.toUpdate
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CategoryServiceImpl(
        private val categoryRepository: CategoryRepository,
        private val userRepository: UserRepository,
        private val authService: AuthService
) : CategoryService {

    override fun createCategory(categoryDto: CategoryDto): CategoryDto {
        val currentUserEmail = authService.getCurrentLoggedUser()

        val user = userRepository.findByEmail(currentUserEmail)
                ?: throw NotFoundException("User Not Found!")
        val category = categoryRepository.save(categoryDto.toCategory(user))
        return category.toCategoryDto()
    }

    override fun getCategoriesByUser(email: String): List<CategoryDto> {
        return categoryRepository.findByUserEmail(email)
                .map { category -> category.toCategoryDto() }
    }

    override fun deleteCategory(id: Int): MessageResponse {
        val category = categoryRepository.findById(id)
                .orElseThrow {  throw NotFoundException("Category Not Found") }
        val currentUserEmail = authService.getCurrentLoggedUser()

        if (category.user.email != currentUserEmail) {
            throw UnauthorizedException("User Not allowed to Delete!")
        }
        categoryRepository.delete(category)
        return MessageResponse("Category Deleted!")
    }

    override fun updateCategory(categoryDto: CategoryDto, id: Int): CategoryDto {

        val category = categoryRepository.findById(id)
                .orElseThrow {  throw NotFoundException("Category Not Found") }

        val currentUserEmail = authService.getCurrentLoggedUser()

        if (category.user.email != currentUserEmail) {
            throw UnauthorizedException("User Not allowed to Update!")
        }
        return categoryRepository.save(category.toUpdate(categoryDto)).toCategoryDto();
    }

}