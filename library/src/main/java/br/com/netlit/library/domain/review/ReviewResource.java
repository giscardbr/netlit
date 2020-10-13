package br.com.netlit.library.domain.review;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class ReviewResource {

	private final ReviewEntityRepository reviewEntityRepository;

	public ReviewResource(final ReviewEntityRepository ReviewEntityRepository) {
		this.reviewEntityRepository = ReviewEntityRepository;
	}

	public Optional<ReviewEntity> byId(final String email, final UUID bookId) {
		return this.reviewEntityRepository.findByIds(email, bookId);
	}

	public void save(ReviewEntity reviewEntity) {
		this.reviewEntityRepository.save(reviewEntity);
	}
}
