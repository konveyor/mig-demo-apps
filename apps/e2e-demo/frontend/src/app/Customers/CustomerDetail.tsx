import * as React from 'react';
import {
  PageSection,
  Card,
  CardBody,
  CardTitle,
  Grid,
  GridItem,
  Title,
  DescriptionList,
  DescriptionListTerm,
  DescriptionListGroup,
  DescriptionListDescription
} from '@patternfly/react-core';

const CustomerDetail: React.FunctionComponent<any> = (props) => {

  const [userData, setUserData] = React.useState<any>();

  const getCustomerData = () => fetch(`/customers-api/customers/${props.computedMatch.params.id}`);

  React.useEffect(() => {

    getCustomerData()
      .then(response => response.json())
      .then(data => {
        setUserData(data)
      })
      .catch((error) => {
        console.error('Error:', error);
      });

  }, []);

  return (
    <PageSection>
      <Title className="pf-u-mb-sm" headingLevel="h1">Details for customer resource</Title>
      {userData && (
        <Grid>
          <GridItem rowSpan={6} smRowSpan={12}>
            <Card isFlat>
              <Grid className="pf-m-all-6-col-on-md">
                <GridItem>
                  <CardTitle>
                    <Title headingLevel="h2">{userData.name} {userData.surname}</Title>
                  </CardTitle>
                  <CardBody>
                    <DescriptionList>
                      <DescriptionListGroup>
                        <DescriptionListTerm>City</DescriptionListTerm>
                        <DescriptionListDescription>{userData.city}</DescriptionListDescription>
                      </DescriptionListGroup>
                      <DescriptionListGroup>
                        <DescriptionListTerm>Country</DescriptionListTerm>
                        <DescriptionListDescription>
                          {userData.country}
                        </DescriptionListDescription>
                      </DescriptionListGroup>
                      <DescriptionListGroup>
                        <DescriptionListTerm>Zipcode</DescriptionListTerm>
                        <DescriptionListDescription>{userData.zipcode}</DescriptionListDescription>
                      </DescriptionListGroup>
                      <DescriptionListGroup>
                        <DescriptionListTerm>User name</DescriptionListTerm>
                        <DescriptionListDescription>{userData.username}</DescriptionListDescription>
                      </DescriptionListGroup>
                    </DescriptionList>
                  </CardBody>
                </GridItem>
              </Grid>
            </Card>
          </GridItem>
        </Grid>
      )}
    </PageSection>
  )
}

export { CustomerDetail };
