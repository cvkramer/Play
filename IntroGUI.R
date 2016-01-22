# Tutorial : 
# http://www.r-bloggers.com/creating-guis-in-r-with-gwidgets/
# Creating GUIs in R with gWidgets

# install.packages( c("gWidgets", "gWidgetstcltk") )
library(gWidgets)
library(gWidgetstcltk) #or gWidgetsGtk2 or gWidgetsrJava or gWidgetsWWW or gWidgetsQt

# Creates the window
win <- gwindow("Tab delimited file upload example")

# By default widgets within window are stacked vertically
# We can create groups of widgets that are stacked horizontally 
# ... with ggroup(which is a widget in itself). 
grp_name <- ggroup(container = win)

# A glabel is a widget that represents a text label.
# Notice that it is contained inside the group we just created.
lbl_data_frame_name <- glabel(
  "Variable to save data to: ",
  container = grp_name
)

# A gedit is a single line textbox. 
# gtext() is a multiline textbox
txt_data_frame_name <- gedit("dfr", container = grp_name)

# Make another horizontal group
grp_upload <- ggroup(container = win) # Notice no visible change in window

# For widgets that we want to respond to an action, we need to add a handler argument.
# This is always a function accepting a list as its first argument (named h by convention), and dots.
# The gbutton handler is called whenever the button is clicked.
# Don’t worry about the contents of the handler function for now; we’ll add them in a moment.
btn_upload <- gbutton(
  text      = "Upload tab delimited file",
  container = grp_upload,
  handler   = function(h, ...)
{
  gfile(
    text    = "Upload tab delimited file",
    type    = "open",
    action  = ifelse(svalue(chk_eurostyle), "read.delim2", "read.delim"),
    handler = function(h, ...)
    {
      tryCatch(
        {
          data_frame_name <- make.names(svalue(txt_data_frame_name))
          the_data <- do.call(h$action, list(h$file))
          assign(data_frame_name, the_data, envir = globalenv())
          svalue(status_bar) <-
            paste(nrow(the_data), "records saved to variable", data_frame_name)
        },
        error = function(e) svalue(status_bar) <- "Could not upload data"
      )
    },
    filter = list(
      "Tab delimited" = list(patterns = c("*.txt","*.dlm","*.tab")),
      "All files" = list(patterns = c("*"))
    )
  )
}
)

# Check box to determine delimiter "." or ","
# Get system default : use_comma_for_decimal
use_comma_for_decimal <- function()
{
  unname(Sys.localeconv()["decimal_point"] == ",")
}
 
chk_eurostyle <- gcheckbox(
  text      = "Use comma for decimal place",
  checked   = use_comma_for_decimal(),
  container = grp_upload
)

# Now include status bar
status_bar <- gstatusbar("", container = win)

# Finally, here’s the content for the button handler. It creates a file open dialog box, which in turn has its own handler function. The action argument names the function to be applied to the file that is opened. The svalue function returns the “most useful thing” from a widget. For a checkbox, the svalue is whether or not it is checked. For a textbox or status bar, the svalue is its text. The filter argument populates the “Files of type” drop down list in the file open dialog.
function(h, ...)
{
  gfile(
    text    = "Upload tab delimited file",
    type    = "open",
    action  = ifelse(svalue(chk_eurostyle), "read.delim2", "read.delim"),
    handler = function(h, ...)
    {
      tryCatch(
        {
          data_frame_name <- make.names(svalue(txt_data_frame_name))
          the_data <- do.call(h$action, list(h$file))
          assign(data_frame_name, the_data, envir = globalenv())
          svalue(status_bar) <-
            paste(nrow(the_data), "records saved to variable", data_frame_name)
        },
        error = function(e) svalue(status_bar) <- "Could not upload data"
      )
    },
    filter = list(
      "Tab delimited" = list(patterns = c("*.txt","*.dlm","*.tab")),
      "All files" = list(patterns = c("*"))
    )
  )
}

# One last trick to finish the post: 
# You can create a GUI interface to any function using ggenericwidget. 
# Try: 
lmwidget <- ggenericwidget(lm)
plotwidget <- ggenericwidget(plot)
