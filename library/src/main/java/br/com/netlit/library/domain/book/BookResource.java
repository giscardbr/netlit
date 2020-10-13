package br.com.netlit.library.domain.book;

import static java.lang.String.format;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import nl.siegmann.epublib.epub.EpubReader;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import br.com.netlit.library.domain.adapter.AdapterRepository;
import br.com.netlit.library.domain.author.AuthorRepository;
import br.com.netlit.library.domain.awards.AwardsRepository;
import br.com.netlit.library.domain.bookmarks.BookmarkEntityRepository;
import br.com.netlit.library.domain.bookmarks.BookmarkRestController;
import br.com.netlit.library.domain.illustrator.IlustratorRepository;
import br.com.netlit.library.domain.page.PageRepository;
import br.com.netlit.library.domain.read.book.ReadBook;
import br.com.netlit.library.domain.read.book.ReadBookRepository;
import br.com.netlit.library.domain.review.ReviewEntity;
import br.com.netlit.library.domain.review.ReviewEntityRepository;
import br.com.netlit.library.domain.translator.TranslatorRepository;
import br.com.netlit.library.infra.error.EntityNotFoundException;
import br.com.netlit.library.infra.error.SendFileException;
import br.com.netlit.library.infra.utils.HttpRequest;
import br.com.netlit.library.infra.utils.UUIDs;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class BookResource {

    private final BookRepository bookRepository;
    private final PageRepository pageRepository;
    private final ReadBookRepository readBookRepository;
    private final BookmarkEntityRepository bookmarkEntityRepository;
    private final ReviewEntityRepository reviewEntityRepository;
    private final String bookAddress;
    private final String bookAddressContent;
    private final String awsRegion;
    private final String awsBucketName;
    private final TranslatorRepository translatorRepository;
    private final AuthorRepository authorRepository;
    private final AwardsRepository awardsRepository;
    private final IlustratorRepository ilustratorRepository;
    private final AdapterRepository adapterRepository;

    public BookResource(
            final BookRepository bookRepository,
            final PageRepository pageRepository,
            final ReadBookRepository readBookRepository,
            final BookmarkEntityRepository bookmarkEntityRepository,
            final ReviewEntityRepository reviewEntityRepository,
            @Value("${library.book-address}") final String bookAddress,
            @Value("${library.book-address-content}") final String bookAddressContent,
            @Value("${aws.region}") final String awsRegion,
            @Value("${aws.s3-bucket-name}") final String awsBucketName,
            TranslatorRepository translatorRepository, AuthorRepository authorRepository, AwardsRepository awardsRepository, IlustratorRepository ilustratorRepository, AdapterRepository adapterRepository) {
        this.bookRepository = bookRepository;
        this.pageRepository = pageRepository;
        this.readBookRepository = readBookRepository;
        this.bookmarkEntityRepository = bookmarkEntityRepository;
        this.reviewEntityRepository = reviewEntityRepository;
        this.bookAddress = bookAddress;
        this.bookAddressContent = bookAddressContent;
        this.awsRegion = awsRegion;
        this.awsBucketName = awsBucketName;
        this.translatorRepository = translatorRepository;
        this.authorRepository = authorRepository;
        this.awardsRepository = awardsRepository;
        this.ilustratorRepository = ilustratorRepository;
        this.adapterRepository = adapterRepository;
    }

    public PagedResources<Resource<Book>> all(final BookQuery query, final Pageable pageable, final PagedResourcesAssembler<Book> assembler) {
        final Page<Book> books = this.bookRepository.findAll(query.toSpecification(), pageable);

        for (final Book book : books) {
            final val id = book.getUuid();
            book.setCoverImageLink(format("%s%s", this.bookAddress, book.getCoverImageLink()));
            book.setAgeGroup(null);
            book.setCollection(null);
            book.setCrossCuttingThemes(null);
            book.setDisciplinaryContents(null);
            book.setEbsaCode(null);
            book.setGuidingTheme(null);
            book.setIllustrator(null);
            book.setIsbn(null);
            book.setReview(null);
            book.setSegment(null);
            book.setSubjects(null);
            book.setTranslator(null);
            book.setYear(null);
            book.setSpecialDates(null);
            book.setAwards(null);
            book.add(linkTo(methodOn(BookRestController.class).findById(id)).withSelfRel());
            book.add(linkTo(methodOn(BookRestController.class).findById(id)).withRel("book"));
        }

        return assembler.toResource(books, linkTo(BookRestController.class).withSelfRel());
    }

    public Resources<Book> all(final List<String> ids) {
        final Iterable<Book> books = this.bookRepository.findAllById(ids);

        for (final Book book : books) {
            final val id = book.getUuid();
            book.setCoverImageLink(format("%s%s", this.bookAddress, book.getCoverImageLink()));
            book.setAgeGroup(null);
            book.setCollection(null);
            book.setCrossCuttingThemes(null);
            book.setDisciplinaryContents(null);
            book.setEbsaCode(null);
            book.setGuidingTheme(null);
            book.setIllustrator(null);
            book.setIsbn(null);
            book.setReview(null);
            book.setSegment(null);
            book.setSubjects(null);
            book.setTranslator(null);
            book.setYear(null);
            book.setSpecialDates(null);
            book.setAwards(null);
            book.add(linkTo(methodOn(BookRestController.class).findById(id)).withSelfRel());
            book.add(linkTo(methodOn(BookRestController.class).findById(id)).withRel("book"));
        }

        return new Resources<>(books);
    }

    public Resource<Book> bookByPageId(final String id) {
        final Book book = UUIDs.fromString(id).map(UUID::toString).flatMap(this.pageRepository::findById).map(br.com.netlit.library.domain.page.Page::getBook).orElseThrow(EntityNotFoundException::new);
        final String currentId = book.getUuid();
        return this.byId(currentId);
    }

    public List<Book> findByAgeGroup(final Integer age) {
//        Iterable<Book> books = bookRepository.findAll();
        List<Book> bookList = bookRepository.findBooksByAgeGroupContains(""+age);

        bookList = bookList.stream().peek(book -> {
            book.setCoverImageLink(format("%s%s", this.bookAddress, book.getCoverImageLink()));
        }).collect(Collectors.toList());

        return bookList.stream()
                .filter(book -> age == Integer.parseInt(book.getAgeGroup().replaceAll("\\D+","")))
                .collect(Collectors.toList());
    }

    public List<Book> findBooksByListId (List<String> uuids) {
        return bookRepository.findBooksByUuidIn(uuids);
    }


    public Resource<Book> byId(final String id) {
        final Book book = UUIDs.fromString(id).map(UUID::toString).flatMap(this.bookRepository::findById).orElseThrow(EntityNotFoundException::new);
        final Optional<UUID> authenticatedAccountId = HttpRequest.getAuthenticatedAccountId();

        authenticatedAccountId
                .map(UUID::toString)
                .map(accountId -> accountId + ":" + id)
                .flatMap(this.readBookRepository::findById)
                .map(ReadBook::getPercentage)
                .ifPresent(book::setPercentageRead);

        book.setBookLink(String.format("%s%s", this.bookAddressContent, book.getCoverImageLink().split("\\/")[1]));
        book.setCoverImageLink(format("%s%s", this.bookAddress, book.getCoverImageLink()));
        final Resource<Book> resource = new Resource<>(book,
                linkTo(methodOn(BookRestController.class).findById(id)).withSelfRel(),
                linkTo(methodOn(BookRestController.class).findById(id)).withRel("book"),
                linkTo(methodOn(BookRestController.class).findCurrentPage(id)).withRel("current_page"),
                linkTo(methodOn(BookRestController.class).findFirstPage(id)).withRel("first_page"));

        authenticatedAccountId
                .flatMap(accountId -> this.bookmarkEntityRepository.findByIds(accountId, UUID.fromString(id)))
                .ifPresent(bookmarkEntity -> {
                    resource.add(linkTo(methodOn(BookmarkRestController.class).findById(id)).withRel("bookmark"));
                });

        ReviewEntity reviewUser = reviewEntityRepository.findByIds(this.getEmail(), UUID.fromString(book.getUuid())).orElse(new ReviewEntity());
        book.setRating(reviewUser.getRating());

        return resource;
    }

    public void uploadBook(MultipartFile epub) throws IOException {
        EpubReader epubReader = new EpubReader();
        nl.siegmann.epublib.domain.Book epubBook = epubReader.readEpub(epub.getInputStream());

        sendFileToS3(epub.getOriginalFilename(), epub.getInputStream(), awsBucketName);

        String[] splitted = epub.getOriginalFilename().split(".");

        Book bookReached = bookRepository.findBookByFileBookName(epub.getOriginalFilename());
        bookReached.setCoverImageLink("/"+splitted[0]+"/"+epubBook.getCoverImage().getHref());
        bookReached.setNumberOfPages((long) epubBook.getContents().size());
        bookRepository.save(bookReached);
    }

    public void uploadSupplement(MultipartFile pdf) throws IOException {

        sendFileToS3(pdf.getOriginalFilename(), pdf.getInputStream(), "cloud-reader/suplemento");

    }

    public void createBook(Book book) {
        UUID uuid = UUID.randomUUID();
        if(UUIDs.isValid(uuid.toString())) {

            book.setUuid(uuid.toString());

            if(CollectionUtils.isNotEmpty(book.getTranslator())) {
                translatorRepository.saveAll(book.getTranslator());
            }

            if(CollectionUtils.isNotEmpty(book.getAwards())) {
                awardsRepository.saveAll(book.getAwards());
            }

            if(CollectionUtils.isNotEmpty(book.getAuthor())){
                authorRepository.saveAll(book.getAuthor());
            }

            if(CollectionUtils.isNotEmpty(book.getAdapters())){
                adapterRepository.saveAll(book.getAdapters());
            }

            if(CollectionUtils.isNotEmpty(book.getIllustrator())){
                ilustratorRepository.saveAll(book.getIllustrator());
            }

            bookRepository.save(book);

            List<br.com.netlit.library.domain.page.Page> pageEntities = generateListPage(book.getNumberOfPages(), book.getFileBookName(), book);
            pageRepository.saveAll(pageEntities);

            book.setFirstPage(pageEntities.get(0));
            bookRepository.save(book);

            pageEntities.stream().forEach(page -> {

                if (page.getSequence().intValue() != 1)
                    page.setPrevious(pageEntities.get(page.getSequence().intValue() - 2));
                if (page.getSequence().intValue() != book.getNumberOfPages())
                    page.setNext(pageEntities.get(page.getSequence().intValue()));
                pageRepository.save(page);
            });
        }

    }

    private String getEmail() {
        OAuth2Authentication oauth2 = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        String email = (String) oauth2.getPrincipal();
        return email;
    }


    private void sendFileToS3(String filename, InputStream is, String awsBucketName) {

        try {
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion(Regions.US_EAST_1)
                    .build();

            PutObjectRequest request = new PutObjectRequest(awsBucketName, filename, is, new ObjectMetadata());
            s3Client.putObject(request);
        } catch (AmazonServiceException amazonServiceException) {
            String message = String.format(
                    "exception in upload file %s to S3 Bucket %s in region %s",
                    filename,
                    this.awsBucketName,
                    this.awsRegion
            );

            log.error(message, amazonServiceException);
            throw new SendFileException(message, amazonServiceException);
        }
    }

    public  static File multipartToFile(MultipartFile multipart, String fileName) throws IllegalStateException, IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir")+"/"+fileName);
        multipart.transferTo(convFile);
        return convFile;
    }

    private List<br.com.netlit.library.domain.page.Page> generateListPage(Long numberOfPages, String fileBookName, Book book){
        List<br.com.netlit.library.domain.page.Page> list = new ArrayList<>();

        for(long i = 0; i < numberOfPages; i++){
            list.add(br.com.netlit.library.domain.page.Page.builder()
                    .sequence(i+1l)
                    .contentLink(String.format("/%s/assets/%d/page.xhtml", fileBookName,i+1))
                    .book(book)
                    .uuid(UUID.randomUUID().toString()).build());
        }

        return list;

    }
}
