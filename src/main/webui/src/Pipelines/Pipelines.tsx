import * as React from 'react';
import {
    Breadcrumb,
    BreadcrumbItem,
    PageSection,
    Card,
    CardHeader,
    CardBody, Button, Split, SplitItem, Stack, StackItem
} from '@patternfly/react-core';
import {PipelinesTable} from './components/PipelinesTable'
import {useHistory} from "react-router-dom";


const Pipelines = () => (
  <PageSection>
      <Stack hasGutter={true}>
          <StackItem><PipelinesHeader/></StackItem>
          <StackItem isFilled><PipelinesTable/></StackItem>
      </Stack>
  </PageSection>
)

function PipelinesHeader() {
    const history = useHistory();

    function newPipeline() {
        history.push("/new");
    }

    return (
        <Card>
            <CardHeader> <Breadcrumb>
                <BreadcrumbItem to="#" isActive>pipelines</BreadcrumbItem>
            </Breadcrumb>
            </CardHeader>

            <CardBody>

                <Split>
                    <SplitItem>Running Pipelinmes</SplitItem>
                    <SplitItem isFilled>&nbsp;</SplitItem>
                    <SplitItem><Button onClick={newPipeline}>New</Button></SplitItem>
                </Split>


            </CardBody>
        </Card>
    )
}


export { Pipelines };
