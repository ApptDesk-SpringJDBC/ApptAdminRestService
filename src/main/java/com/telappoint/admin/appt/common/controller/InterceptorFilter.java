package com.telappoint.admin.appt.common.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.log4j.Logger;

import com.telappoint.admin.appt.common.constants.PropertiesConstants;
import com.telappoint.admin.appt.common.util.PropertyUtils;


/**
 * 
 * @author Balaji N
 *
 */
public class InterceptorFilter implements javax.servlet.Filter {
	private final Logger logger = Logger.getLogger(this.getClass().getName());

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		try {
			final HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
			BufferedRequestWrapper bufferedRequest = new BufferedRequestWrapper(httpRequest);
			String logRequest = PropertyUtils.getValueFromProperties("LOG_REQUEST", PropertiesConstants.APPT_ADMIN_REST_SERVICE_PROP.getPropertyFileName());
			String logResponse = PropertyUtils.getValueFromProperties("LOG_RESPONSE", PropertiesConstants.APPT_ADMIN_REST_SERVICE_PROP.getPropertyFileName());
			if (logRequest !=null && "Y".equals(logRequest)) {
				String url = httpRequest.getRequestURL().toString();
				if("get".equalsIgnoreCase(httpRequest.getMethod())) {
					url = url+"?"+httpRequest.getQueryString();
					System.out.println("Request: ["+httpRequest.getMethod()+"] - "+url);
				} else {
					System.out.println("Request ==> ["+httpRequest.getMethod()+"] -  	 	"+url);
					System.out.println("\t :"+new String(bufferedRequest.getBuffer()));
				}
			}

			final HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

			final ByteArrayPrintWriter pw = new ByteArrayPrintWriter();
			HttpServletResponse wrappedResp = new HttpServletResponseWrapper(httpResponse) {
				public PrintWriter getWriter() {
					return pw.getWriter();
				}

				public ServletOutputStream getOutputStream() {
					return pw.getStream();
				}
			};

			filterChain.doFilter(bufferedRequest, wrappedResp);

			byte[] bytes = pw.toByteArray();
			httpResponse.getOutputStream().write(bytes);
			if (logResponse !=null && "Y".equals(logResponse))
				System.out.println("Response ==>" + new String(bytes));

		} catch (Exception e) {
			logger.error("Exception in logger creation:" + e, e);
		}
	}

	private static class ByteArrayServletStream extends ServletOutputStream {
		ByteArrayOutputStream baos;
		ByteArrayServletStream(ByteArrayOutputStream baos) {
			this.baos = baos;
		}
		public void write(int param) throws IOException {
			baos.write(param);
		}
	}

	private static class ByteArrayPrintWriter {
		private ByteArrayOutputStream baos = new ByteArrayOutputStream();
		private PrintWriter pw = new PrintWriter(baos);
		private ServletOutputStream sos = new ByteArrayServletStream(baos);

		public PrintWriter getWriter() {
			return pw;
		}

		public ServletOutputStream getStream() {
			return sos;
		}

		byte[] toByteArray() {
			return baos.toByteArray();
		}
	}

	private class BufferedServletInputStream extends ServletInputStream {
		ByteArrayInputStream bais;
		public BufferedServletInputStream(ByteArrayInputStream bais) {
			this.bais = bais;
		}

		public int available() {
			return bais.available();
		}

		public int read() {
			return bais.read();
		}

		public int read(byte[] buf, int off, int len) {
			return bais.read(buf, off, len);
		}

	}

	private class BufferedRequestWrapper extends HttpServletRequestWrapper {
		ByteArrayInputStream bais;
		ByteArrayOutputStream baos;
		BufferedServletInputStream bsis;

		byte[] buffer;

		public BufferedRequestWrapper(HttpServletRequest req) throws IOException {
			super(req);
			InputStream is = req.getInputStream();
			baos = new ByteArrayOutputStream();
			byte buf[] = new byte[1024];
			int letti;
			while ((letti = is.read(buf)) > 0) {
				baos.write(buf, 0, letti);
			}
			buffer = baos.toByteArray();
		}

		public ServletInputStream getInputStream() {
			try {
				bais = new ByteArrayInputStream(buffer);
				bsis = new BufferedServletInputStream(bais);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return bsis;
		}

		public byte[] getBuffer() {
			return buffer;
		}
	}

	@Override
	public void destroy() {
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}
}
