package org.iitg.mobileprofiler.helpers;

public class EmailAccount {
	public String urlServer = "gmail.com";
	public String username = "mobilebtpiitg";
	public String password = "afroninja";
	public String emailAddress;

	public EmailAccount() {
		this.emailAddress = username + "@" + urlServer;
	}
}
