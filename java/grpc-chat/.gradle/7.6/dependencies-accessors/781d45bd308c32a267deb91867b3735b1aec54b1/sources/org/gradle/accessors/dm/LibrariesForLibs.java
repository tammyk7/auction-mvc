package org.gradle.accessors.dm;

import org.gradle.api.NonNullApi;
import org.gradle.api.artifacts.MinimalExternalModuleDependency;
import org.gradle.plugin.use.PluginDependency;
import org.gradle.api.artifacts.ExternalModuleDependencyBundle;
import org.gradle.api.artifacts.MutableVersionConstraint;
import org.gradle.api.provider.Provider;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ProviderFactory;
import org.gradle.api.internal.catalog.AbstractExternalDependencyFactory;
import org.gradle.api.internal.catalog.DefaultVersionCatalog;
import java.util.Map;
import javax.inject.Inject;

/**
 * A catalog of dependencies accessible via the `libs` extension.
*/
@NonNullApi
public class LibrariesForLibs extends AbstractExternalDependencyFactory {

    private final AbstractExternalDependencyFactory owner = this;
    private final AnnotationsLibraryAccessors laccForAnnotationsLibraryAccessors = new AnnotationsLibraryAccessors(owner);
    private final GrpcLibraryAccessors laccForGrpcLibraryAccessors = new GrpcLibraryAccessors(owner);
    private final VersionAccessors vaccForVersionAccessors = new VersionAccessors(providers, config);
    private final BundleAccessors baccForBundleAccessors = new BundleAccessors(objects, providers, config);
    private final PluginAccessors paccForPluginAccessors = new PluginAccessors(providers, config);

    @Inject
    public LibrariesForLibs(DefaultVersionCatalog config, ProviderFactory providers, ObjectFactory objects) {
        super(config, providers, objects);
    }

    /**
     * Returns the group of libraries at annotations
     */
    public AnnotationsLibraryAccessors getAnnotations() { return laccForAnnotationsLibraryAccessors; }

    /**
     * Returns the group of libraries at grpc
     */
    public GrpcLibraryAccessors getGrpc() { return laccForGrpcLibraryAccessors; }

    /**
     * Returns the group of versions at versions
     */
    public VersionAccessors getVersions() { return vaccForVersionAccessors; }

    /**
     * Returns the group of bundles at bundles
     */
    public BundleAccessors getBundles() { return baccForBundleAccessors; }

    /**
     * Returns the group of plugins at plugins
     */
    public PluginAccessors getPlugins() { return paccForPluginAccessors; }

    public static class AnnotationsLibraryAccessors extends SubDependencyFactory {

        public AnnotationsLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for api (org.apache.tomcat:annotations-api)
             * This dependency was declared in settings file 'settings.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getApi() { return create("annotations.api"); }

    }

    public static class GrpcLibraryAccessors extends SubDependencyFactory {

        public GrpcLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for netty (io.grpc:grpc-netty-shaded)
             * This dependency was declared in settings file 'settings.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getNetty() { return create("grpc.netty"); }

            /**
             * Creates a dependency provider for protobuf (io.grpc:grpc-protobuf)
             * This dependency was declared in settings file 'settings.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getProtobuf() { return create("grpc.protobuf"); }

            /**
             * Creates a dependency provider for stub (io.grpc:grpc-stub)
             * This dependency was declared in settings file 'settings.gradle'
             */
            public Provider<MinimalExternalModuleDependency> getStub() { return create("grpc.stub"); }

    }

    public static class VersionAccessors extends VersionFactory  {

        public VersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: grpc (1.52.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in settings file 'settings.gradle'
             */
            public Provider<String> getGrpc() { return getVersion("grpc"); }

            /**
             * Returns the version associated to this alias: tomcat (6.0.53)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in settings file 'settings.gradle'
             */
            public Provider<String> getTomcat() { return getVersion("tomcat"); }

    }

    public static class BundleAccessors extends BundleFactory {

        public BundleAccessors(ObjectFactory objects, ProviderFactory providers, DefaultVersionCatalog config) { super(objects, providers, config); }

    }

    public static class PluginAccessors extends PluginFactory {

        public PluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

    }

}
