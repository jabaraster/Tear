<?xml version="1.0"?>

<TrackingData>
	<Sensors>
		<Sensor ="FeatureBasedSensorSource" Subtype="Fast">

			<SensorID>FeatureTracking1</SensorID>

			<Parameters>
				<FeatureDescriptorAlignment>regular</FeatureDescriptorAlignment>
				<MaxObjectsToDetectPerFrame>5</MaxObjectsToDetectPerFrame>
				<MaxObjectsToTrackInParallel>1</MaxObjectsToTrackInParallel>
				<SimilarityThreshold>0.7</SimilarityThreshold>
			</Parameters>

			<SensorCOS>
				<SensorCosID>Patch1</SensorCosID>
				<Parameters>
					<ReferenceImage WidthMM="80" HeightMM="80">marker01.png</ReferenceImage>
					<SimilarityThreshold>0.7</SimilarityThreshold>
				</Parameters>
			</SensorCOS>

			<SensorCOS>
				<SensorCosID>Patch2</SensorCosID>
				<Parameters>
					<ReferenceImage WidthMM="160" HeightMM="160">marker02.png</ReferenceImage>
					<SimilarityThreshold>0.7</SimilarityThreshold>
				</Parameters>
			</SensorCOS>

		</Sensor>
	</Sensors>

	<Connections>
		<COS>
			<Name>MarkerlessCOS1</Name>
			<Fuser Type="SmoothingFuser">
				<Parameters>
					<KeepPoseForNumberOfFrames>2</KeepPoseForNumberOfFrames>
					<GravityAssistance></GravityAssistance>
					<AlphaTranslation>0.8</AlphaTranslation>
					<GammaTranslation>0.8</GammaTranslation>
					<AlphaRotation>0.5</AlphaRotation>
					<GammaRotation>0.5</GammaRotation>
					<ContinueLostTrackingWithOrientationSensor>false</ContinueLostTrackingWithOrientationSensor>
				</Parameters>
			</Fuser>

			<SensorSource>
				<SensorID>FeatureTracking1</SensorID>
				<SensorCosID>Patch1</SensorCosID>
				<HandEyeCalibration>
					<TranslationOffset>
						<X>0</X>
						<Y>0</Y>
						<Z>0</Z>
					</TranslationOffset>
					<RotationOffset>
						<X>0</X>
						<Y>0</Y>
						<Z>0</Z>
						<W>1</W>
					</RotationOffset>
				</HandEyeCalibration>
				<COSOffset>
					<TranslationOffset>
						<X>0</X>
						<Y>0</Y>
						<Z>0</Z>
					</TranslationOffset>
					<RotationOffset>
						<X>0</X>
						<Y>0</Y>
						<Z>0</Z>
						<W>1</W>
					</RotationOffset>
				</COSOffset>
			</SensorSource>
		</COS>

		<COS>
			<Name>MarkerlessCOS2</Name>
			<Fuser Type="BestQualityFuser">
				<Parameters>
					<KeepPoseForNumberOfFrames>2</KeepPoseForNumberOfFrames>
					<GravityAssistance></GravityAssistance>
					<AlphaTranslation>0.8</AlphaTranslation>
					<GammaTranslation>0.8</GammaTranslation>
					<AlphaRotation>0.5</AlphaRotation>
					<GammaRotation>0.5</GammaRotation>
					<ContinueLostTrackingWithOrientationSensor>false</ContinueLostTrackingWithOrientationSensor>
				</Parameters>
			</Fuser>

			<SensorSource>
				<SensorID>FeatureTracking1</SensorID>
				<SensorCosID>Patch2</SensorCosID>
				<HandEyeCalibration>
					<TranslationOffset>
						<X>0</X>
						<Y>0</Y>
						<Z>0</Z>
					</TranslationOffset>
					<RotationOffset>
						<X>0</X>
						<Y>0</Y>
						<Z>0</Z>
						<W>1</W>
					</RotationOffset>
				</HandEyeCalibration>
				<COSOffset>
					<TranslationOffset>
						<X>0</X>
						<Y>0</Y>
						<Z>0</Z>
					</TranslationOffset>
					<RotationOffset>
						<X>0</X>
						<Y>0</Y>
						<Z>0</Z>
						<W>1</W>
					</RotationOffset>
				</COSOffset>
			</SensorSource>
		</COS>

	</Connections>
</TrackingData>
