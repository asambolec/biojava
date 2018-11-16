/**
 *                    BioJava development code
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  If you do not have a copy,
 * see:
 *
 *      http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright for this code is held jointly by the individual
 * authors.  These should be listed in @author doc comments.
 *
 * For more information on the BioJava project and its aims,
 * or to join the biojava-l mailing list, visit the home page
 * at:
 *
 *      http://www.biojava.org/
 *
 * Created on Aug 30, 2011
 * Created by Andreas Prlic
 *
 * @since 3.0.2
 */
package org.biojava.nbio.structure.scop;

import org.biojava.nbio.structure.align.client.JFatCatClient;
import org.biojava.nbio.structure.align.util.URLConnectionTools;
import org.biojava.nbio.structure.scop.server.ScopDescriptions;
import org.biojava.nbio.structure.scop.server.ScopDomains;
import org.biojava.nbio.structure.scop.server.ScopNodes;
import org.biojava.nbio.structure.scop.server.XMLUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class that fetches information about SCOP from a remote data-source. It
 * requires port 80 to open for HTTP connection.
 *
 * @author Andreas Prlic
 *
 */
public class RemoteScopInstallation implements ScopDatabase {

	private static final Logger logger = LoggerFactory.getLogger(RemoteScopInstallation.class);

	public static final String DEFAULT_SERVER = "http://source.rcsb.org/jfatcatserver/domains/";

	String server = DEFAULT_SERVER;

	private String version = null;

	public static void main(String[] args) {

		ScopDatabase scop = new RemoteScopInstallation();
		ScopFactory.setScopDatabase(scop);

		// System.out.println(scop.getByCategory(ScopCategory.Superfamily));

		logger.info(String.valueOf(scop.getDomainsForPDB("4HHB")));
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	@Override
	public List<ScopDescription> getByCategory(ScopCategory category) {
		List<ScopDescription> results = null;
		try {
			URL u = new URL(new StringBuilder().append(server).append("getByCategory?category=").append(category)
					.append("&version=").append(getScopVersion()).toString());
			InputStream response = URLConnectionTools.getInputStream(u);
			String xml = JFatCatClient.convertStreamToString(response);

			if (!xml.trim().isEmpty()) {
				ScopDescriptions container = ScopDescriptions.fromXML(xml);
				results = container.getScopDescription();
			}
		} catch (IOException e) {
			throw new RuntimeException(
					new StringBuilder().append("Unable to reach ").append(server).append("getByCategory?category=")
							.append(category).append("&version=").append(getScopVersion()).toString(),
					e);
		}
		return results;
	}

	@Override
	public List<ScopDescription> filterByClassificationId(String query) {
		List<ScopDescription> results = null;
		try {
			URL u = new URL(new StringBuilder().append(server).append("filterByClassificationId?query=").append(query)
					.append("&version=").append(getScopVersion()).toString());
			InputStream response = URLConnectionTools.getInputStream(u);
			String xml = JFatCatClient.convertStreamToString(response);

			if (!xml.trim().isEmpty()) {
				ScopDescriptions container = ScopDescriptions.fromXML(xml);
				results = container.getScopDescription();
			}
		} catch (Exception e) {
			throw new RuntimeException(new StringBuilder().append("Unable to reach ").append(server)
					.append("filterByClassificationId?query=").append(query).append("&version=")
					.append(getScopVersion()).toString(), e);
		}
		return results;
	}

	@Override
	public List<ScopNode> getTree(ScopDomain domain) {
		List<ScopNode> results = null;
		try {
			URL u = new URL(new StringBuilder().append(server).append("getTree?scopId=").append(domain.getScopId())
					.append("&version=").append(getScopVersion()).toString());
			InputStream response = URLConnectionTools.getInputStream(u);
			String xml = JFatCatClient.convertStreamToString(response);

			if (!xml.trim().isEmpty()) {
				ScopNodes container = ScopNodes.fromXML(xml);
				results = container.getScopNode();
			}
		} catch (Exception e) {
			throw new RuntimeException(
					new StringBuilder().append("Unable to reach ").append(server).append("getTree?scopId=")
							.append(domain.getScopId()).append("&version=").append(getScopVersion()).toString(),
					e);
		}
		return results;
	}

	@Override
	public List<ScopDomain> filterByDomainName(String query) {
		query = query.trim();
		List<ScopDomain> results = null;
		try {
			URL u = new URL(new StringBuilder().append(server).append("filterByDomainName?query=").append(query)
					.append("&version=").append(getScopVersion()).toString());
			// System.out.println(u);
			InputStream response = URLConnectionTools.getInputStream(u);
			String xml = JFatCatClient.convertStreamToString(response);

			if (!xml.trim().isEmpty()) {
				ScopDomains container = ScopDomains.fromXML(xml);
				results = container.getScopDomain();
			}
		} catch (Exception e) {
			throw new RuntimeException(
					new StringBuilder().append("Unable to reach ").append(server).append("filterByDomainName?query=")
							.append(query).append("&version=").append(getScopVersion()).toString(),
					e);
		}
		return results;
	}

	@Override
	public List<ScopDescription> filterByDescription(String query) {
		List<ScopDescription> results = null;
		try {
			URL u = new URL(new StringBuilder().append(server).append("filterByDescription?query=").append(query)
					.append("&version=").append(getScopVersion()).toString());
			InputStream response = URLConnectionTools.getInputStream(u);
			String xml = JFatCatClient.convertStreamToString(response);

			if (!xml.trim().isEmpty()) {
				ScopDescriptions container = ScopDescriptions.fromXML(xml);
				results = container.getScopDescription();
			}
		} catch (Exception e) {
			throw new RuntimeException(
					new StringBuilder().append("Unable to reach ").append(server).append("filterByDescription?query=")
							.append(query).append("&version=").append(getScopVersion()).toString(),
					e);
		}
		return results;
	}

	@Override
	public ScopDescription getScopDescriptionBySunid(int sunid) {

		ScopDescription desc = null;

		try {

			URL u = new URL(new StringBuilder().append(server).append("getScopDescriptionBySunid?sunid=").append(sunid)
					.append("&version=").append(getScopVersion()).toString());
			InputStream response = URLConnectionTools.getInputStream(u);
			String xml = JFatCatClient.convertStreamToString(response);

			if (!xml.trim().isEmpty()) {
				desc = XMLUtil.getScopDescriptionFromXML(xml);
			}

		} catch (Exception e) {
			throw new RuntimeException(new StringBuilder().append("Unable to reach ").append(server)
					.append("getScopDescriptionBySunid?sunid=").append(sunid).append("&version=")
					.append(getScopVersion()).toString(), e);
		}
		return desc;
	}

	@Override
	public List<ScopDomain> getDomainsForPDB(String pdbId) {
		List<ScopDomain> results = null;
		try {
			URL u = new URL(new StringBuilder().append(server).append("getDomainsForPDB?pdbId=").append(pdbId)
					.append("&version=").append(getScopVersion()).toString());
			InputStream response = URLConnectionTools.getInputStream(u);
			String xml = JFatCatClient.convertStreamToString(response);

			if (!xml.trim().isEmpty()) {
				ScopDomains container = ScopDomains.fromXML(xml);
				results = container.getScopDomain();
			}
		} catch (Exception e) {
			throw new RuntimeException(
					new StringBuilder().append("Unable to reach ").append(server).append("getDomainsForPDB?pdbId=")
							.append(pdbId).append("&version=").append(getScopVersion()).toString(),
					e);
		}
		return results;
	}

	private ScopDomain requestRemoteDomainByScopID(String scopId) throws IOException {
		scopId = scopId.trim();
		URL u = new URL(new StringBuilder().append(server).append("getDomainByScopID?scopId=").append(scopId)
				.append("&version=").append(getScopVersion()).toString());
		InputStream response = URLConnectionTools.getInputStream(u);
		String xml = JFatCatClient.convertStreamToString(response);

		if (!xml.trim().isEmpty()) {
			return XMLUtil.getScopDomainFromXML(xml);
		}
		return null;
	}

	@Override
	public ScopDomain getDomainByScopID(String scopId) {
		try {
			return requestRemoteDomainByScopID(scopId);
		} catch (Exception e) {
			throw new RuntimeException(
					new StringBuilder().append("Unable to reach ").append(server).append("getDomainByScopID?scopId=")
							.append(scopId).append("&version=").append(getScopVersion()).toString(),
					e);
		}
	}

	@Override
	public ScopNode getScopNode(int sunid) {
		ScopNode desc = null;
		try {
			URL u = new URL(new StringBuilder().append(server).append("getScopNode?sunid=").append(sunid)
					.append("&version=").append(getScopVersion()).toString());
			InputStream response = URLConnectionTools.getInputStream(u);
			String xml = JFatCatClient.convertStreamToString(response);

			if (!xml.trim().isEmpty()) {
				desc = XMLUtil.getScopNodeFromXML(xml);
			}
		} catch (Exception e) {
			throw new RuntimeException(new StringBuilder().append("Unable to reach ").append(server)
					.append("getScopNode?sunid=").append(sunid).append("&version=").append(getScopVersion()).toString(),
					e);
		}
		return desc;
	}

	@Override
	public String getScopVersion() {
		// If no version is set, request the default version from the website
		if (version == null) {
			try {
				URL u = new URL(server + "getScopVersion");
				InputStream response = URLConnectionTools.getInputStream(u);
				version = JFatCatClient.convertStreamToString(response);
				if (version != null) {
					version = version.trim();
				}

			} catch (Exception e) {
				throw new RuntimeException(new StringBuilder().append("Unable to reach ").append(server)
						.append("getScopVersion").toString(), e);
			}
		}
		return version;
	}

	@Override
	public void setScopVersion(String version) {
		this.version = version;
	}

	@Override
	public List<ScopDomain> getScopDomainsBySunid(Integer sunid) {
		List<ScopDomain> results = null;
		try {
			URL u = new URL(new StringBuilder().append(server).append("getScopDomainsBySunid?sunid=").append(sunid)
					.append("&version=").append(getScopVersion()).toString());
			InputStream response = URLConnectionTools.getInputStream(u);
			String xml = JFatCatClient.convertStreamToString(response);

			if (!xml.trim().isEmpty()) {
				ScopDomains container = ScopDomains.fromXML(xml);
				results = container.getScopDomain();
			}
		} catch (Exception e) {
			throw new RuntimeException(
					new StringBuilder().append("Unable to reach ").append(server).append("getScopDomainsBySunid?sunid=")
							.append(sunid).append("&version=").append(getScopVersion()).toString(),
					e);
		}
		return results;
	}

	@Override
	public List<String> getComments(int sunid) {
		List<String> results = null;
		try {
			URL u = new URL(new StringBuilder().append(server).append("getComments?sunid=").append(sunid)
					.append("&version=").append(getScopVersion()).toString());
			InputStream response = URLConnectionTools.getInputStream(u);
			String xml = JFatCatClient.convertStreamToString(response);

			if (!xml.trim().isEmpty()) {
				results = XMLUtil.getCommentsFromXML(xml);
			}
		} catch (Exception e) {
			throw new RuntimeException(new StringBuilder().append("Unable to reach ").append(server)
					.append("getComments?sunid=").append(sunid).append("&version=").append(getScopVersion()).toString(),
					e);
		}
		return results;
	}

}
