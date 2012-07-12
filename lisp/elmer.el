;;; elmer.el --- Code for use with elmer paste service

;; Copyright (C) 2011-2012 Drew Raines (me AT draines DOT com)

;; Emacs Lisp Archive Entry
;; Filename: elmer.el
;; Version: 1.0
;; Date: Thu 13-Jul-2012
;; Keywords: data
;; Author: Drew Raines (me AT draines DOT com)
;; Maintainer: Drew Raines (me AT draines DOT com)
;; Description: Code for use with elmer paste service
;; URL: http://www.draines.com/src/
;; Compatibility: Emacs24

;; This file is not part of GNU Emacs.

;; This is free software; you can redistribute it and/or modify it under
;; the terms of the GNU General Public License as published by the Free
;; Software Foundation; either version 2, or (at your option) any later
;; version.
;;
;; This is distributed in the hope that it will be useful, but WITHOUT
;; ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
;; FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
;; for more details.
;;
;; You should have received a copy of the GNU General Public License
;; along with GNU Emacs; see the file COPYING.  If not, write to the
;; Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston,
;; MA 02111-1307, USA.

(defgroup elmer nil
  "Interface to the elmer paste service."
  :group 'data)

(defcustom elmer-paste-bin "/usr/local/bin/elmer"
  "Location of script that accepts stdin and knows how to paste
to elmer."
  :type 'string
  :group 'elmer)

(defun elmer-concat-cmd (str1 str2)
  (if (string= str2 "")
      str1
    (concat str1 " " str2)))

(defun elmer (name key)
  (interactive "MName (defaults to random): \nMKey: ")
  (let* ((buf (get-buffer-create "*elmer*"))
         (cmd (elmer-concat-cmd elmer-paste-bin name))
         (cmd (if (not (string= name ""))
                  (elmer-concat-cmd cmd key)
                cmd))
         (resp (let ((str (save-excursion
                            (shell-command-on-region (mark) (point) cmd buf)
                            (set-buffer buf)
                            (buffer-string))))
                 (if (string-match "\\(\n\\|\\s-\\)+$" str)
                     (replace-match "" t t str)
                   str)))
         (url (if (string-match " http" resp)
                  (kill-new (car (last (split-string resp)))))))
    resp))

(provide 'elmer)
