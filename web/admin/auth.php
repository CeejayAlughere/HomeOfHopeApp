<?php
session_start();
require_once 'config.php';
require_once 'helpers.php';

// Check if user is logged in and session hasn't expired
if (!is_logged_in()) {
    // If not logged in or timed out, destroy session and redirect to login
    session_unset();
    session_destroy();
    header("Location: login.php");
    exit();
}

// Update last activity time
$_SESSION['last_activity'] = time();
?>
