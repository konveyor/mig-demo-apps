import * as React from 'react';
import {
  Button,
  PageSection,
  Card,
  CardBody,
  CardTitle,
  Grid,
  GridItem,
  BackgroundImage,
  Title,
  DescriptionList,
  DescriptionListTerm,
  DescriptionListGroup,
  DescriptionListDescription
} from '@patternfly/react-core';
import { WikipediaWIcon } from '@patternfly/react-icons';

const ProductDetail: React.FunctionComponent<any> = (props) => {

  const [productData, setProductData] = React.useState<any>();

  const getProductData = () => fetch(`/products-api/products/${props.computedMatch.params.slug}`);

  React.useEffect(() => {
    getProductData()
    .then(response => response.json())
    .then(productData => {
      console.log(productData);
      setProductData(productData)
    });

  }, []);

  return (
    <PageSection>
      <Title className="pf-u-mb-sm" headingLevel="h1">Details for product resource</Title>
      {productData && (
        <Grid>
          <GridItem rowSpan={6} smRowSpan={12}>
            <Card isFlat>
              <Grid>
                <GridItem>
                  <CardBody>
                    <DescriptionList>
                      <DescriptionListGroup>
                        <DescriptionListTerm>Name</DescriptionListTerm>
                        <DescriptionListDescription>{productData.name}</DescriptionListDescription>
                      </DescriptionListGroup>

                      <DescriptionListGroup>
                        <DescriptionListTerm>ID</DescriptionListTerm>
                        <DescriptionListDescription>{productData.id}</DescriptionListDescription>
                      </DescriptionListGroup>

                      <DescriptionListGroup>
                        <DescriptionListTerm>Description</DescriptionListTerm>
                        <DescriptionListDescription>{productData.description}</DescriptionListDescription>
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

export { ProductDetail };
