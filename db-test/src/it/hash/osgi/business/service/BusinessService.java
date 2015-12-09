package it.hash.osgi.business.service;

import it.hash.osgi.business.Business;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

//import javax.servlet.http.HttpServletRequest;

public interface BusinessService {
	Map<String, Object> login(String businessname, String password);
	Map<String, Object> login(Map<String, Object> pars);
	Map<String, Object> loginByOAuth2(Map<String, Object> pars);
	List<Business> listBusinesss();
	Map<String,Object> testToken(String token);
	Map<String, Object> validateBusinessname(String businessId, String businessname);
	Map<String, Object> validateEMail(String businessId, String email);
	Map<String, Object> validateMobile(String businessId, String mobile);
	Map<String, Object> validateIdentificator(String identificator);
	String generatePassword();
	Map<String, Object> getBusiness(Map<String, Object> pars);
	Map<String, Object> create(Business business);
	void createBusinesssByCSV(BufferedReader reader, boolean simulation, boolean activation) throws IOException;
	Map<String, Object> deleteBusiness(Map<String, Object> pars);
	Map<String, Object> updateBusiness(Map<String, Object> pars);
	List<Business> getBusinessDetails(Map<String, Object> pars);
	List<Business> searchBusinesss(String parameter);
//	Map<String, Object> update(HttpServletRequest request);
	List<Business> getBusinesss();
	void addBusinesssByCSV(BufferedReader reader, boolean simulation, boolean activation) throws IOException;
	Map<String, Object> addBusiness(Business business);
}
