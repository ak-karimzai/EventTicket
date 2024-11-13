package com.akkarimzai.eventticket.repositories.specs

import com.akkarimzai.eventticket.entities.Event
import com.akkarimzai.eventticket.entities.Event_
import jakarta.persistence.criteria.Predicate
import org.springframework.data.jpa.domain.Specification
import java.time.LocalDateTime

object EventSpecification {
    fun buildSpecification(title: String?, artist: String?, from: LocalDateTime?, to: LocalDateTime?): Specification<Event> {
        return Specification<Event> { root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()

            title?.let {
                predicates.add(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get(Event_.title)), "%${it.lowercase()}%")
                )
            }

            artist?.let {
                predicates.add(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get(Event_.artist)), "%${it.lowercase()}%")
                )
            }

            from?.let {
                predicates.add(
                    criteriaBuilder.greaterThanOrEqualTo(root.get(Event_.date), it)
                )
            }

            to?.let {
                predicates.add(
                    criteriaBuilder.lessThanOrEqualTo(root.get(Event_.date), it)
                )
            }

            criteriaBuilder.and(*predicates.toTypedArray())
        }
    }
}