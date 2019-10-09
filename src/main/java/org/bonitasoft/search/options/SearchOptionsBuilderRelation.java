package org.bonitasoft.search.options;

import java.io.Serializable;
import java.util.List;

import org.bonitasoft.engine.search.Order;
import org.bonitasoft.engine.search.SearchOptionsBuilder;
import org.bonitasoft.engine.search.Sort;
import org.bonitasoft.engine.search.impl.SearchFilter;
import org.bonitasoft.engine.search.impl.SearchOptionsImpl;


public class SearchOptionsBuilderRelation  {

    private final SearchOptionsRelationImpl options;


    /**
     * Builds a new {@link SearchOptions} with results limited to {@code startIndex} and {@code maxResults}. If you are interested only in the number of
     * elements matching with the given criteria without knowing the elements details, it's possible to use zero as {@code maxResults}:
     * {@link SearchResult#getResult()} will send an empty list and {@link SearchResult#getCount()} will return the number of matching elements.
     *
     * @param startIndex the first result to return
     * @param maxResults the maximum results to return. The actual number can be smaller, if the end of the list has been reached.
     * @see SearchOptions
     * @see SearchResult#getResult()
     * @see SearchResult#getCount()
     */
    public SearchOptionsBuilderRelation(final int startIndex, final int maxResults) {
        options = new SearchOptionsRelationImpl(startIndex, maxResults);
    }

    /**
     * Creates a new <code>SearchOptionsBuilder</code> from another instance by
     *
     * @param searchOptions
     */
    public SearchOptionsBuilderRelation(final SearchOptionsRelationImpl searchOptions) {
        options = new SearchOptionsRelationImpl(searchOptions.getStartIndex(), searchOptions.getMaxResults());
        options.setFilters(searchOptions.getFilters());
        options.setSorts(searchOptions.getSorts());
        options.setSearchTerm(searchOptions.getSearchTerm());
    }

    /**
     * Filter the results to the specific value for the specific field (equality)
     *
     * @param field
     *        The name of the field to filter on. Depending on the search parameter, specify the field by accessing the relevant xxxSearchDescriptor classes.
     *        For example, <code>HumanTaskInstanceSearchDescriptor.NAME</code> and <code>HumanTaskInstanceSearchDescriptor.PROCESS_DEFINITION_ID</code>.
     * @param value
     *        the single value to filter on that field name
     * @return this builder itself
     * @since 6.0
     */
    public SearchOptionsBuilderRelation filter(final String field, final Serializable value) {
        options.addFilter(field, value);
        return this;
    }

    /**
     * Filters search results with a greaterThan comparison operation.
     *
     * @param field
     *        the field name to compare to.
     * @param value
     *        the value to compare.
     * @return this builder itself
     * @see SearchOptionsBuilder#filter(String, java.io.Serializable) for field values
     */
    public SearchOptionsBuilderRelation greaterThan(final String field, final Serializable value) {
        options.addGreaterThanFilter(field, value);
        return this;
    }

    /**
     * @param field
     * @param value
     * @return this builder itself
     * @see SearchOptionsBuilder#filter(String, java.io.Serializable) for field values
     */
    public SearchOptionsBuilderRelation greaterOrEquals(final String field, final Serializable value) {
        options.addGreaterOrEqualsFilter(field, value);
        return this;
    }

    /**
     * @param field the field that should be less than
     * @param value
     * @return this builder itself
     * @see SearchOptionsBuilder#filter(String, java.io.Serializable) for field values
     */
    public SearchOptionsBuilderRelation lessThan(final String field, final Serializable value) {
        options.addLessThanFilter(field, value);
        return this;
    }

    /**
     * @param field the field that should be less or equals
     * @param value the value
     * @return this builder itself
     * @see SearchOptionsBuilder#filter(String, java.io.Serializable) for field values
     */
    public SearchOptionsBuilderRelation lessOrEquals(final String field, final Serializable value) {
        options.addLessOrEqualsFilter(field, value);
        return this;
    }

    /**
     * @param field the field that should be between
     * @param from from this value
     * @param to to this value
     * @return this builder itself
     * @see SearchOptionsBuilder#filter(String, java.io.Serializable) for field values
     */
    public SearchOptionsBuilderRelation between(final String field, final Serializable from, final Serializable to) {
        options.addBetweenFilter(field, from, to);
        return this;
    }

    /**
     * @param field
     * @param value
     * @return this builder itself
     * @see SearchOptionsBuilder#filter(String, java.io.Serializable) for field values
     */
    public SearchOptionsBuilderRelation differentFrom(final String field, final Serializable value) {
        options.addDifferentFromFilter(field, value);
        return this;
    }

    /**
     * @return this builder itself
     */
    public SearchOptionsBuilderRelation or() {
        if (options.getFilters().size() == 0) {
            throw new IllegalArgumentException("OR operator cannot be the first filter in the list.");
        }
        options.addOrFilter();
        return this;
    }

    /**
     * @return this builder itself
     */
    public SearchOptionsBuilderRelation and() {
        if (options.getFilters().size() == 0) {
            throw new IllegalArgumentException("AND operator cannot be the first filter in the list.");
        }
        options.addAndFilter();
        return this;
    }

    public SearchOptionsBuilderRelation leftParenthesis() {
        options.addLeftParenthesis();
        return this;
    }

    public SearchOptionsBuilderRelation rightParenthesis() {
        options.addRightParenthesis();
        return this;
    }

    /**
     * @param field the field that should be between
     * @param from from this value
     * @param to to this value
     * @return this builder itself
     * @see SearchOptionsBuilder#filter(String, java.io.Serializable) for field values
     */
    public SearchOptionsBuilderRelation relation(final String fieldRelation) {
        options.addRelation(fieldRelation);
        return this;
    }
    /**
     * @param value the search term
     * @return this builder itself
     */
    public SearchOptionsBuilderRelation searchTerm(final String value) {
        options.setSearchTerm(value);
        return this;
    }

    /**
     * Adds a sort order option to the list of sort options
     *
     * @param field
     *        the field name to sort by
     * @param order
     *        the order of the sort (ASCENDING, DESCENDING)
     * @return the current SearchOptionsBuilder
     */
    public SearchOptionsBuilderRelation sort(final String field, final Order order) {
        options.addSort(field, order);
        return this;
    }

    /**
     * @param filters the filters to set
     * @return this builder itself
     */
    public SearchOptionsBuilderRelation setFilters(final List<SearchFilter> filters) {
        options.setFilters(filters);
        return this;
    }

    /**
     * @param sorts the sorts to set
     * @return this builder itself
     */
    public SearchOptionsBuilderRelation setSort(final List<Sort> sorts) {
        options.setSorts(sorts);
        return this;
    }

    /**
     * @return the <code>SearchOptions</code> finally built using this builder.
     */
    public SearchOptionsRelationImpl done() {
        return options;
    }
}

    
  

