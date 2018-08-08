package org.sotorrent.condor.resources;

/**
 * Class for developer resource "AndroidReference".
 * It matches links to official Android documentation resources.
 */
public class AndroidReference extends DeveloperResource
{
    AndroidReference()
    {
        super("android.com");

        createResourcePattern(new String[]{
                "^https?://(www\\.)?(d(eveloper)?\\.)?android\\.com(/intl/[^/]+)?(/ndk)?(?!.*(/reference/|/google/)).*",
                "^https?://(www\\.)?(s(ource)?\\.)?android.com(/intl/[^/]+)?(?!(/reference/|/google/)).*",
                "^https?://(www\\.)?(tools\\.)?android.com.*"
        });
    }
}
