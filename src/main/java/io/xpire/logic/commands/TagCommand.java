package io.xpire.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import io.xpire.commons.core.Messages;
import io.xpire.commons.core.index.Index;
import io.xpire.logic.commands.exceptions.CommandException;
import io.xpire.model.Model;
import io.xpire.model.StackManager;
import io.xpire.model.item.Item;
import io.xpire.model.item.XpireItem;
import io.xpire.model.state.State;
import io.xpire.model.tag.Tag;
import io.xpire.model.tag.TagComparator;
import io.xpire.model.tag.TagItemDescriptor;

//@@author Kalsyc
/**
 * Adds tag(s) to xpireItem identified using its displayed index from the expiry date tracker.
 */
public class TagCommand extends Command {



    /**
     * Private enum to indicate whether command shows all tags or tags and xpireItem
     */
    enum TagMode { SHOW, TAG }

    public static final String COMMAND_WORD = "tag";
    public static final String COMMAND_SHORTHAND = "t";

    public static final String MESSAGE_USAGE = COMMAND_WORD

            + ": Tags the xpireItem identified by the index number used in the displayed item list.\n"
            + "Note that only 5 tags are allowed per item. \n"
            + "Format: <index>|<tag>[<other tags>]...\n"
            + "(index must be a positive integer; each tag must be prefixed with a '#')\n"
            + "Example: " + COMMAND_WORD + "|1|#Food #Fruit";

    public static final String MESSAGE_TAG_ITEM_SUCCESS = "Tagged item: %1$s";
    public static final String MESSAGE_TAG_SHOW_SUCCESS = "All item tags:";
    public static final String MESSAGE_TAG_SHOW_FAILURE = "There are no tags.";
    private static final String MESSAGE_TOO_MANY_TAGS = "Only 5 tags are allowed per item.";
    private static final String MESSAGE_TAG_ITEM_SUCCESS_TRUNCATION_WARNING = "Warning! "
            + "Some tag(s) have been truncated. Maximum tag length accepted is 20.\nTagged item: %1$s";

    private final Index index;
    private final TagItemDescriptor tagItemDescriptor;
    private final TagMode mode;
    private boolean containsLongTags = false;
    private Item item = null;
    private String result = "";



    public TagCommand(Index index, TagItemDescriptor tagItemDescriptor) {
        this.index = index;
        this.tagItemDescriptor = new TagItemDescriptor(tagItemDescriptor);
        this.mode = TagMode.TAG;
    }

    public TagCommand(Index index, String[] str) {
        this.index = index;
        this.tagItemDescriptor = new TagItemDescriptor();
        this.tagItemDescriptor.setTags(Arrays.stream(str).map(Tag::new).collect(Collectors.toSet()));
        this.mode = TagMode.TAG;
    }

    public TagCommand() {
        this.index = null;
        this.tagItemDescriptor = null;
        this.mode = TagMode.SHOW;
    }

    public TagMode getMode() {
        return this.mode;
    }

    @Override
    public CommandResult execute(Model model, StackManager stackManager) throws CommandException {
        requireNonNull(model);
        List<? extends Item> lastShownList = model.getCurrentFilteredItemList();

        switch (this.mode) {
        case TAG:
            if (this.index.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_ITEM_DISPLAYED_INDEX);
            }
            XpireItem xpireItemToTag = (XpireItem) lastShownList.get(this.index.getZeroBased());
            this.item = xpireItemToTag;
            XpireItem taggedXpireItem = createTaggedItem(xpireItemToTag, this.tagItemDescriptor);
            if (this.tagItemDescriptor.getTags().stream().anyMatch(Tag::isTruncated)) {
                this.containsLongTags = true;
            }
            stackManager.saveState(new State(model));
            model.setItem(xpireItemToTag, taggedXpireItem);
            this.showInHistory = true;
            if (containsLongTags) {
                this.result = String.format(MESSAGE_TAG_ITEM_SUCCESS_TRUNCATION_WARNING, taggedXpireItem);
                return new CommandResult(this.result);
            }
            this.result = String.format(MESSAGE_TAG_ITEM_SUCCESS, taggedXpireItem);
            return new CommandResult(this.result);

        case SHOW:
            Set<Tag> tagSet = new TreeSet<>(new TagComparator());
            List<XpireItem> xpireItemList = model.getAllItemList();
            xpireItemList.forEach(item -> tagSet.addAll(item.getTags()));
            if (tagSet.isEmpty()) {
                return new CommandResult(MESSAGE_TAG_SHOW_FAILURE);
            }
            List<String> tagNameList = tagSet.stream().map(Tag::toString).collect(Collectors.toList());
            StringBuilder str = appendTagsToFeedback(tagNameList, new StringBuilder(MESSAGE_TAG_SHOW_SUCCESS));
            return new CommandResult(str.toString());

        default:
        }
        throw new CommandException(Messages.MESSAGE_UNKNOWN_COMMAND);
    }


    /**
     * Appends tags to user feedback to show all tags.
     *
     * @param tagNameList List of tag names.
     * @param str StringBuilder to append to.
     */
    public static StringBuilder appendTagsToFeedback(List<String> tagNameList, StringBuilder str) {
        for (String tagName: tagNameList) {
            str.append("\n").append(tagName);
        }
        return str;
    }

    /**
     * Creates and returns a {@code XpireItem} with the details of {@code xpireItemToTag}
     * edited with {@code tagItemDescriptor}.
     */
    private static XpireItem createTaggedItem(XpireItem xpireItemToTag, TagItemDescriptor tagItemDescriptor)
            throws CommandException {
        assert xpireItemToTag != null;
        Set<Tag> updatedTags = updateTags(xpireItemToTag, tagItemDescriptor);
        if (updatedTags.size() >= 6) {
            throw new CommandException(MESSAGE_TOO_MANY_TAGS);
        }
        return new XpireItem(xpireItemToTag.getName(), xpireItemToTag.getExpiryDate(), xpireItemToTag.getQuantity(),
                updatedTags, xpireItemToTag.getReminderThreshold());
    }

    /**
     * Creates and returns a {@code XpireItem} with the details of {@code xpireItemToTag}
     * edited with {@code tagItemDescriptor}.
     */
    private static Item createTaggedReplenishItem(Item xpireItemToTag, TagItemDescriptor tagItemDescriptor) {
        assert xpireItemToTag != null;
        Set<Tag> updatedTags = updateTags(xpireItemToTag, tagItemDescriptor);
        return new Item(xpireItemToTag.getName(), updatedTags);
    }

    /**
     * Returns an updated set of tags.
     *
     * @param xpireItemToTag XpireItem to be tagged.
     * @param tagItemDescriptor Descriptor that specifies additional tags to be added on or tags to be cleared.
     * @return Set containing updated tags.
     */
    private static Set<Tag> updateTags(Item xpireItemToTag, TagItemDescriptor tagItemDescriptor) {
        Set<Tag> set = new TreeSet<>(new TagComparator());
        set.addAll(xpireItemToTag.getTags());
        set.addAll(tagItemDescriptor.getTags());
        return set;
    }

    @Override
    public int hashCode() {
        return this.tagItemDescriptor.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof TagCommand)) {
            return false;
        }

        // state check
        TagCommand e = (TagCommand) other;
        if (mode.equals(TagMode.SHOW)) {
            return mode.equals(e.mode);
        }
        return index.equals(e.index)
                && tagItemDescriptor.equals(e.tagItemDescriptor)
                && mode.equals(e.mode);
    }

    @Override
    public String toString() {
        return "the following Tag command:\n" + this.result;
    }

}
