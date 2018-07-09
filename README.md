# Namara Java SDK

To build:

`docker build -t namara-java-sdk .`

To run:

`docker run -it namara-java-sdk sh`

# Reporting Issues

Please create a ticket in the Issues page for this repo, or reach out to us directly (see *Get in Touch*). 

# Contributing

Please see the Issues page for list of outstanding bugs. If you are looking to implement a feature outside of the list of bugs
please create an Issue for the feature that you would like to implement.

Assign it to yourself or comment that you will be taking it over. Then:

1. Fork this repo
2. Clone your repo and check out a branch based off of development. The name of the branch should be `feature/<ticket number>`
3. Implement your feature. Ensure that
    1. You have updated/incremented the version in `Dockerfile` and `pom.xml`
    1. Tests are passing and the `.jar` file compiles
    2. You have added javadoc and regenerated the html files
4. Check out [our sample application](https://github.com/thinkdata-works/sample_apps/tree/develop/java-example). 
Pull your compiled `namara-<version>.jar` file into a sample and verify that it works. If you would like to update the sample application to include
your new feature or fix, feel free to create a PR in that repo.
5. Open a pull request from your fork to our repo on the development branch. 
    1. Title: `feature/<ticket number> <General summary of work done>`
    2. Description: `Fixes #<ticket number>. <Additional details of work>`

# Get in touch

Please reach out to us at anytime at `support@namara.io`