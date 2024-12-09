package com.akkarimzai.eventticket.repositories.specs

import com.akkarimzai.eventticket.entities.Category_
import com.akkarimzai.eventticket.entities.Event_
import com.akkarimzai.eventticket.entities.Ticket
import com.akkarimzai.eventticket.entities.Ticket_
import jakarta.persistence.criteria.Predicate
import org.springframework.data.jpa.domain.Specification

object TicketSpecification {
    fun buildSpecification(categoryId: Int?, eventId: Int?, title: String?): Specification<Ticket> {
        return Specification<Ticket> { root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()

            categoryId?.let {
                predicates.add(
                    criteriaBuilder.equal(root.get(Ticket_.event).get(Event_.category).get(Category_.id), it)
                )
            }

            eventId?.let {
                predicates.add(criteriaBuilder.equal(root.get(Ticket_.event).get(Event_.id), it))
            }

            title?.let {
                predicates.add(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get(Ticket_.title)), "%${it.lowercase()}%")
                )
            }

            criteriaBuilder.and(*predicates.toTypedArray())
        }
    }
}
