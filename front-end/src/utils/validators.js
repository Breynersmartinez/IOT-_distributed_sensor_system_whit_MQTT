export const validators = {
  validateNodeId: (nodeId) => {
    return nodeId && nodeId.trim().length > 0;
  },

  validateNodeName: (nodeName) => {
    return nodeName && nodeName.trim().length > 0;
  },

  validateLocation: (location) => {
    return location && location.trim().length > 0;
  },

  validateMqttTopic: (topic) => {
    return topic && topic.trim().length > 0 && topic.includes('/');
  },

  validateNodeData: (nodeData) => {
    return (
      validators.validateNodeId(nodeData.nodeId) &&
      validators.validateNodeName(nodeData.nodeName) &&
      validators.validateLocation(nodeData.location) &&
      validators.validateMqttTopic(nodeData.mqttTopic)
    );
  },
};