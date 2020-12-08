package Example.OkHttpTesting;

import java.io.IOException;
import java.net.URI;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.Scanner;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class App {

	static OkHttpClient client = getUnsafeOkHttpClient();
	public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

	public static void main(String[] args) throws Exception {
		System.out.println("Starting");
		System.out.println("Give input");
		Scanner sc = new Scanner(System.in);
		String fName = sc.next();
		String lName = sc.next();

		Request request = new Request.Builder().url("https://localhost:8443/testing?fullName=" + fName + "+" + lName)
				.build();

		System.out.println("connected");
		
		
		Response response = client.newCall(request).execute();
		Protocol prt = response.protocol();
		
		String en =  prt.name();
		System.out.println(en);
		System.out.println("Get request");
		System.out.println(response.body().string());

		System.out.println("Post Request");
		String json = nameJson(fName, lName);
	    RequestBody body = RequestBody.create(json, JSON);
	    Request request2 = new Request.Builder()
	            .url("https://localhost:8443/testing")
	            .post(body)
	            .build();
	    Response response2 = client.newCall(request2).execute();
	    System.out.println(response2.body().string());
	}

	static String nameJson(String fName, String lName) {
		return "{'fullName''" + fName + " " + lName + "'}";
	}

	private static OkHttpClient getUnsafeOkHttpClient() {
		try {
			// Create a trust manager that does not validate certificate chains
			final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

				public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
					// TODO Auto-generated method stub

				}

				public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
					// TODO Auto-generated method stub

				}

				public X509Certificate[] getAcceptedIssuers() {
					// TODO Auto-generated method stub
					return new X509Certificate[0];
				}

			} };

			// Install the all-trusting trust manager
			final SSLContext sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
			// Create an ssl socket factory with our all-trusting manager
			final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

			return new OkHttpClient.Builder().sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0])
					.hostnameVerifier(new HostnameVerifier() {

						public boolean verify(String arg0, SSLSession arg1) {
							// TODO Auto-generated method stub
							return true;
						}
					}).build();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
