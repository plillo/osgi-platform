package it.hash.osgi.aws.console;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.DescribeAvailabilityZonesResult;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.DomainMetadataRequest;
import com.amazonaws.services.simpledb.model.DomainMetadataResult;
import com.amazonaws.services.simpledb.model.ListDomainsRequest;
import com.amazonaws.services.simpledb.model.ListDomainsResult;

public class ShellCommands {
	public volatile Console console;
	
	public void ec2() {
		try {
			AmazonEC2 ec2 = new AmazonEC2Client(console.getCredentials());
	        ec2.setEndpoint("ec2.eu-central-1.amazonaws.com");
	        // Availability zone
	        // -----------------
	        DescribeAvailabilityZonesResult availabilityZonesResult = ec2.describeAvailabilityZones();
	        System.out.println(""
	        		+ "AVAILABILITY ZONES"
	        		+ "\n------------------"
	        		+ "\nYou have access to " 
	        		+ availabilityZonesResult.getAvailabilityZones().size() 
	        		+ " Availability Zones.");
	        
	        // Instances
	        // ---------
            DescribeInstancesResult describeInstancesRequest = ec2.describeInstances();
            List<Reservation> reservations = describeInstancesRequest.getReservations();
            Set<Instance> instances = new HashSet<Instance>();
            for (Reservation reservation : reservations) {
                instances.addAll(reservation.getInstances());
            }
            System.out.println(""
	        		+ "INSTANCES"
	        		+ "\n---------"
            		+ "\nYou have " 
		            + instances.size() 
		            + " Amazon EC2 instance(s) running.");
            // instances states
            if(instances.size()>0)
	            for (Instance instance : instances) {
	                String iid = instance.getInstanceId();
	                String state = instance.getState().getName();
	                System.out.println("Instance " + iid + "- state:"+state);
	            }
		}
		catch(Throwable e){
			e.printStackTrace();
		}
	}
	
	public void s3() {
	     /*
         * Amazon S3
         *
         * The AWS S3 client allows you to manage buckets and programmatically
         * put and get objects to those buckets.
         *
         * In this sample, we use an S3 client to iterate over all the buckets
         * owned by the current user, and all the object metadata in each
         * bucket, to obtain a total object and space usage count. This is done
         * without ever actually downloading a single object -- the requests
         * work with object metadata only.
         */
        try {
        	AmazonS3Client s3  = new AmazonS3Client(console.getCredentials());
            List<Bucket> buckets = s3.listBuckets();

            long totalSize  = 0;
            int  totalItems = 0;
            for (Bucket bucket : buckets) {

            	ObjectListing objects = s3.listObjects(bucket.getName());
                do {
                    for (S3ObjectSummary objectSummary : objects.getObjectSummaries()) {
                        totalSize += objectSummary.getSize();
                        totalItems++;
                    }
                    objects = s3.listNextBatchOfObjects(objects);
                } while (objects.isTruncated());
            }

            System.out.println(""
	        		+ "S3 BUCKETS"
	        		+ "\n----------"
            		+ "\nYou have " + buckets.size() + " Amazon S3 bucket(s), " +
                    "containing " + totalItems + " objects with a total size of " + totalSize + " bytes.");
        } catch (AmazonServiceException ase) {
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Error Message: " + ace.getMessage());
        }
	}
	
	public void sdb() {
        /*
         * Amazon SimpleDB
         *
         * The AWS SimpleDB client allows you to query and manage your data
         * stored in SimpleDB domains (similar to tables in a relational DB).
         *
         * In this sample, we use a SimpleDB client to iterate over all the
         * domains owned by the current user, and add up the number of items
         * (similar to rows of data in a relational DB) in each domain.
         */
        try {
        	AmazonSimpleDBClient sdb = new AmazonSimpleDBClient(console.getCredentials());
            
            ListDomainsRequest sdbRequest = new ListDomainsRequest().withMaxNumberOfDomains(100);
            ListDomainsResult sdbResult = sdb.listDomains(sdbRequest);

            int totalItems = 0;
            for (String domainName : sdbResult.getDomainNames()) {
                DomainMetadataRequest metadataRequest = new DomainMetadataRequest().withDomainName(domainName);
                DomainMetadataResult domainMetadata = sdb.domainMetadata(metadataRequest);
                totalItems += domainMetadata.getItemCount();
            }

            System.out.println(""
	        		+ "SimpleDB"
	        		+ "\n--------"
            		+ "\nYou have " + sdbResult.getDomainNames().size() + " Amazon SimpleDB domain(s)" +
                    "containing a total of " + totalItems + " items.");
        } catch (AmazonServiceException ase) {
                System.out.println("Caught Exception: " + ase.getMessage());
                System.out.println("Reponse Status Code: " + ase.getStatusCode());
                System.out.println("Error Code: " + ase.getErrorCode());
                System.out.println("Request ID: " + ase.getRequestId());
        }
	}
}
