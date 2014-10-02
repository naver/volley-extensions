package com.navercorp.volleyextensions.volleyer.response.parser;

import com.navercorp.volleyextensions.volleyer.http.ContentTypes;
/**
 * <pre>
 * An interface that defines supporting content types for a parser.
 * NOTE: IntegratedNetworkResponseParser needs supporing types for choosing a right parser.
 *       For details, see {@link IntegratedNetworkResponseParser}.
 * </pre>
 * @see NetworkResponseParser
 * @see IntegratedNetworkResponseParser
 */
public interface TypedNetworkResponseParser extends NetworkResponseParser {
	/**
	 * Return content types that NetworkResponseParser can parse.
	 * @see ContentTypes
	 */
	ContentTypes getContentTypes();
}
