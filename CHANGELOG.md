# Changelog

# [2.1.0](https://github.com/clarin-eric/fcs-simple-client/releases/tag/2.1.0) - 2024-02-01

- Additions:
  - Add `<Institution>` element to `<Resource>`s in `<EndpointDescription>`.
  - Add [Github Pages](https://clarin-eric.github.io/fcs-simple-client/) with [JavaDoc](https://clarin-eric.github.io/fcs-simple-client/project-reports.html)
  - Add Changelog document
  
  * Add convenience for checking for checking type
  * Add utility class to auto-detect the FCS versionn supported by an endpoint
  * Add _experimental_ basic support for FCS authentication

- Bug Fixes
  - fix NPE when parsing malformed FCS 2.0 endpoint descriptions

- Changes:
  * Change to XPath based `<EndpointDescription>` parsing in `ClarinFCSEndpointDescriptionParser`.
    This means that the whole XML subtree will be loaded in memory compared to the prior stream based parsing and it requires more time and memory.
    It however also allows to better handle _broken_ Endpoint Descriptions, e.g., due to spec updates and newer/older library versions.
    Switching to the older stream based parsing is still possible and might be necessary for some use-cases (too many resources on endpoints)

  - Emit debugging output of parsed capabilities
  - Be strict when parsing FCS 2.0 endpoint descriptions
  - Add some useful constants
  - Move some constants to ClarinFCSConstants to make them public
  - More complete legacy compat mode (to be used in FCS conformance tester)

- Dependencies:
  - Update to latest (`2.2.0`) [`fcs-sru-client`](https://github.com/clarin-eric/fcs-sru-client) library
  - SLF4j to `1.7.32`
  - Bump Maven build plugin versions

- General:
  - Cleanup, whitespaces, copyright

For older changes, see commit history at [https://github.com/clarin-eric/fcs-simple-client/commits/main/](https://github.com/clarin-eric/fcs-simple-client/commits/main/?after=6055b0d7327ceed0c9a56dd51dcd1d077088ff8c+0&branch=main&qualified_name=refs%2Fheads%2Fmain)