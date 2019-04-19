package xpertss.json.schema.core.report;

import xpertss.json.schema.core.processing.Processor;

/**
 * Message providing interface
 *
 * <p>This interface must be implemented by all inputs and outputs used by
 * {@link Processor}s. This allows a processor to grab a context-dependent
 * message for more accurate reporting.</p>
 */
public interface MessageProvider {
    
    /**
     * Provide a message template for this context
     *
     * @return a new processing message
     */
    ProcessingMessage newMessage();
}
